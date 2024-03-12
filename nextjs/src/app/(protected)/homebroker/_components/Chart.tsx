import { useEffect, useRef } from "react"
import { createChart, ColorType, Time, UTCTimestamp } from "lightweight-charts"
import { cn } from "@/lib/utils"
import {
  AreaDatum,
  CandlestickDatum,
} from "@/app/(protected)/homebroker/_components/types"

type ChartProps = {
  type: "area" | "candlestick"
  series: (CandlestickDatum | AreaDatum)[]
  symbol: string
  active: "area" | "candle"
  className?: string
}

const options = {
  layout: {
    background: {
      type: ColorType.Solid,
      color: "rgba(197, 203, 206, 0.0)",
    },
    textColor: "white",
  },
  grid: {
    vertLines: {
      color: "rgba(197, 203, 206, 0.3)",
    },
    horzLines: {
      color: "rgba(197, 203, 206, 0.3)",
    },
  },
}

function Chart({ type, series, symbol, active, className }: ChartProps) {
  const chartContainerRef = useRef<HTMLDivElement>(null)
  useEffect(() => {
    if (
      !series ||
      !active ||
      !chartContainerRef.current ||
      !series.map ||
      series.length === 1
    ) {
      console.log("No series")
      return
    }

    const chart = createChart(chartContainerRef.current, {
      ...options,
      width: chartContainerRef.current.clientWidth,
      height: 300,
      timeScale:
        type === "area"
          ? {
              rightOffset: 3,
              barSpacing: 3,
              secondsVisible: true,
              timeVisible: true,
            }
          : undefined,
    })

    let chartSeries
    if (type === "area") {
      chartSeries = chart.addAreaSeries({
        title: "$",
        lineColor: "#3FE8BC",
        topColor: "rgba(63, 232, 188, 0.9)",
        bottomColor: "rgba(63, 232, 188, 0.1)",
      })
    } else if (type === "candlestick") {
      chartSeries = chart.addCandlestickSeries({
        upColor: "#3FE8BC",
        downColor: "#D63A5E",
        borderVisible: false,
        wickUpColor: "#3FE8BC",
        wickDownColor: "#D63A5E",
        title: "$",
      })
    } else {
      console.log("Invalid chart type:", type)
      return
    }
    const mapped = series.map((data) => {
      if (type === "area") {
        const { time, price } = data as AreaDatum
        return {
          time: Math.floor(new Date(time).getTime() / 1000) as UTCTimestamp,
          value: price,
        }
      } else {
        const { bucket, open, high, low, close } = data as CandlestickDatum
        return {
          time: Math.round(new Date(bucket).getTime() / 1000) as Time,
          open,
          high,
          low,
          close,
        }
      }
    })

    // if (type === "area") { // uncomment if you don't have realtime data for today and want to test the area recent chart
    //   const stubData = [
    //     { value: 0, time: 1642425322 },
    //     { value: 8, time: 1642511722 },
    //     { value: 10, time: 1642598122 },
    //     { value: 20, time: 1642684522 },
    //     { value: 3, time: 1642770922 },
    //     { value: 43, time: 1642857322 },
    //     { value: 41, time: 1642943722 },
    //     { value: 43, time: 1643030122 },
    //     { value: 56, time: 1643116522 },
    //     { value: 46, time: 1643202922 },
    //   ]
    //   chartSeries.setData(stubData as any)
    // } else {
    chartSeries.setData(mapped)
    // }

    const handleResize = () => {
      chart.applyOptions({
        ...options,
        width: chartContainerRef.current!.clientWidth,
        height: 300,
      })
    }

    window.addEventListener("resize", handleResize)
    chart.timeScale().fitContent()

    return () => {
      window.removeEventListener("resize", handleResize)
      chart.remove()
    }
  }, [type, series, active])

  if (!active) return null // Return null if not active

  return (
    <div
      className={cn("relative flex-grow   ", className)}
      ref={chartContainerRef}
    >
      <article className="format format-invert absolute left-4 top-0 z-10">
        <div className="text-3xl font-bold text-white">{symbol}</div>
      </article>
    </div>
  )
}

export default Chart
