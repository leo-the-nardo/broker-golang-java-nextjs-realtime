"use client"

import useSWR from "swr"

import { useState } from "react"
import { Card } from "@/components/ui/card"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import AssetSwitcher from "@/components/asset-switcher"
import { fetcherClient, syncCookie } from "@/lib/client-fetcher"
import { cn } from "@/lib/utils"
import * as React from "react"
import useSWRSubscription from "swr/subscription"
import {
  AreaDatum,
  AssetPointComputedEvent,
  CandlestickDatum,
} from "@/app/(protected)/homebroker/_components/types"
import { Icons } from "@/components/icons"
import SkeletonC from "@/app/(protected)/homebroker/_components/skeleton"
import Chart from "@/app/(protected)/homebroker/_components/Chart"
import MobileOrderForm from "@/app/(protected)/homebroker/_components/MobileOrderForm"
import { API_URL } from "@/routes"

type AssetChartComponentProps = {
  symbol: string
}
export function AssetChartComponent({ symbol }: AssetChartComponentProps) {
  const {
    data: candleSeries,
    mutate: mutateCandleSeries,
    error: candleError,
    isLoading: isLoadingCandle,
  } = useSWR<CandlestickDatum[]>(
    `${API_URL}/assets/${symbol}/candle`,
    fetcherClient,
    {
      revalidateOnReconnect: false,
      revalidateOnFocus: false,
      keepPreviousData: true,
      fallbackData: [
        {
          bucket: "",
          open: 0,
          high: 0,
          low: 0,
          close: 0,
        },
      ],
    },
  )
  const {
    data: areaSeries,
    mutate: mutateAreaSeries,
    error: areaError,
    isLoading: isLoadingArea,
  } = useSWR<AreaDatum[]>(`${API_URL}/assets/${symbol}/series`, fetcherClient, {
    revalidateOnReconnect: false,
    revalidateOnFocus: false,
    fallbackData: [
      {
        time: "",
        symbol: "",
        price: 0,
      },
    ],
  })
  const [retry, setRetry] = useState(0)
  useSWRSubscription(
    [retry, `${API_URL}/assets/${symbol}/series/events`],
    ([_, url], { next }) => {
      const eventSource = new EventSource(url, {
        withCredentials: true,
      })
      window.addEventListener("beforeunload", function () {
        eventSource.close()
      })
      eventSource.addEventListener("asset-updated", async (event) => {
        const pointInserted: AssetPointComputedEvent = JSON.parse(event.data)
        await mutateAreaSeries((series) => {
          return [...series!, pointInserted]
        }, false)
        await mutateCandleSeries((series) => {
          const lastIndex = series!.length - 1
          const lastCandle = series![lastIndex]
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
            series![lastIndex] = lastCandle
          }
          if (
            insertedTime.getUTCMonth() !== lastCandleTime.getUTCMonth() ||
            insertedTime.getUTCDate() !== lastCandleTime.getUTCDate()
          ) {
            series!.push({
              bucket: pointInserted.time,
              open: insertedPrice,
              high: insertedPrice,
              low: insertedPrice,
              close: insertedPrice,
            })
          }
          return [...series!]
        }, false)
        next(null, pointInserted)
      })
      eventSource.onerror = (error) => {
        eventSource.close()
        syncCookie().then(() => {
          //for some reason, firefox(only) closes the connection randomly so i need it
          setTimeout(() => {
            setRetry((prev) => prev + 1)
          }, 2000)
        })
      }
      return () => {
        eventSource.close()
      }
    },
    {
      revalidateOnReconnect: false,
      revalidateOnFocus: false,
      revalidateOnMount: false,
      revalidateIfStale: false,
    },
  )
  const [activeTab, setActiveTab] = useState<"area" | "candle">("area")

  return (
    <>
      <Card className="flex  h-full flex-col justify-center gap-4 border border-gray-700/80 bg-transparent px-2 py-6 shadow-lg">
        <Tabs
          defaultValue="area"
          onValueChange={(value) => {
            setActiveTab(value as "area" | "candle")
          }}
        >
          <div className="flex items-center justify-between">
            <div>
              <TabsList>
                <TabsTrigger value="area" className="text-lg">
                  <Icons.areaChart />
                  Recent
                </TabsTrigger>
                <TabsTrigger value="candle" className="text-lg">
                  <Icons.candleChart />
                  Long
                </TabsTrigger>
              </TabsList>
            </div>
            <AssetSwitcher
              defaultAsset={symbol}
              className="border-0 bg-background backdrop-brightness-150 hover:bg-background hover:backdrop-brightness-100"
            />
          </div>
          <TabsContent value="candle" className="">
            {candleSeries!.length == 1 && (
              <SkeletonC className="max-h-[300px]" />
            )}
            <Chart
              className={cn(candleSeries!.length == 1 && "hidden")}
              type="candlestick"
              active={activeTab}
              symbol={symbol}
              series={candleSeries!}
            />
          </TabsContent>
          <TabsContent value="area">
            {areaSeries!.length === 1 && (
              <SkeletonC className="max-h-[300px]" />
            )}
            <Chart
              className={cn(areaSeries!.length == 1 && "hidden")}
              type="area"
              active={activeTab}
              symbol={symbol}
              series={areaSeries!}
            />
          </TabsContent>
        </Tabs>
        <MobileOrderForm assetId={symbol} />
      </Card>
    </>
  )
}
