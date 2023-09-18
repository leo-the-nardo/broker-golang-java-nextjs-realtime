"use client"

import { fetcher } from "@/app/utils"
import useSWR from "swr"
import useSWRSubscription, { SWRSubscriptionOptions } from "swr/subscription"
import { useRef, useState } from "react"
import { Card, TabItem, Tabs } from "flowbite-react"
import { FiActivity, FiBarChart2 } from "@/app/components/react-icons/icons"
import AreaChart from "@/app/components/AreaChart"
import CandleChart from "@/app/components/CandleChart"

type AssetChartComponentProps = {
  symbol: string
}
export function AssetChartComponent({ symbol }: AssetChartComponentProps) {
  const { data: assetInfo } = useSWR(
    `http://localhost:8080/assets/${symbol}`,
    fetcher,
    {
      revalidateOnReconnect: false,
      revalidateOnFocus: false,
      fallbackData: {
        name: "",
        symbol: "",
      },
    },
  )
  const {
    data: candleSeries,
    mutate: mutateCandleSeries,
    error: candleError,
  } = useSWR(`http://localhost:8080/assets/${symbol}/candle`, fetcher, {
    revalidateOnReconnect: false,
    revalidateOnFocus: false,
    fallbackData: [
      {
        time: 0,
        open: 0,
        high: 0,
        low: 0,
        close: 0,
      },
    ],
  })
  const {
    data: areaSeries,
    mutate: mutateAreaSeries,
    error: areaError,
  } = useSWR(`http://localhost:8080/assets/${symbol}/series`, fetcher, {
    revalidateOnReconnect: false,
    revalidateOnFocus: false,
    fallbackData: [
      {
        time: 0,
        symbol: "",
        price: 0,
        dayVolume: 0,
      },
    ],
  })
  const { data: newPoint } = useSWRSubscription(
    `http://localhost:8080/assets/${symbol}/series/events`,
    (url, { next }) => {
      const eventSource = new EventSource(url)
      eventSource.addEventListener("asset-updated", async (event) => {
        const pointInserted = JSON.parse(event.data)
        await mutateAreaSeries((series) => {
          return [...series, pointInserted]
        }, false)
        await mutateCandleSeries((series) => {
          const lastIndex = series.length - 1
          const lastCandle = series[lastIndex]
          const insertedPrice = pointInserted.price
          const insertedTime = new Date(pointInserted.time)
          const lastCandleTime = new Date(lastCandle.bucket)
          if (
            insertedTime.getUTCMonth() === lastCandleTime.getUTCMonth() &&
            insertedTime.getUTCDate() === lastCandleTime.getUTCDate()
          ) {
            if (insertedPrice > lastCandle.high) {
              lastCandle.high = insertedPrice
            }
            if (insertedPrice < lastCandle.low) {
              lastCandle.low = insertedPrice
            }
            lastCandle.close = insertedPrice
            series[lastIndex] = lastCandle
          }
          if (
            insertedTime.getUTCMonth() !== lastCandleTime.getUTCMonth() ||
            insertedTime.getUTCDate() !== lastCandleTime.getUTCDate()
          ) {
            series.push({
              bucket: pointInserted.time,
              open: insertedPrice,
              high: insertedPrice,
              low: insertedPrice,
              close: insertedPrice,
            })
          }
          return [...series]
        }, false)
        next(null, pointInserted)
      })
      eventSource.onerror = (error) => {
        eventSource.close()
      }
      return () => {
        eventSource.close()
      }
    },
  )

  const tabsRef = useRef(null)
  const [activeTab, setActiveTab] = useState(0)

  return (
    <>
      <Card
        className="dark:bg-transparent"
        theme={{
          root: {
            children: "flex h-full flex-col justify-center gap-4 py-4 px-2",
          },
        }}
      >
        <Tabs
          aria-label="Tabs with icons"
          style="underline"
          ref={tabsRef}
          onActiveTabChange={(tab) => setActiveTab(tab)}
        >
          <TabItem active title="Recent" icon={FiActivity}>
            <AreaChart
              active={activeTab === 0}
              symbol={symbol}
              companyName={assetInfo.name}
              series={areaSeries}
            />
          </TabItem>
          <TabItem title="Long" icon={FiBarChart2}>
            <CandleChart
              active={activeTab === 1}
              symbol={symbol}
              companyName={assetInfo.name}
              series={candleSeries}
            />
          </TabItem>
        </Tabs>
      </Card>
    </>
  )
}
