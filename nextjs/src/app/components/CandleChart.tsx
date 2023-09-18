"use client"
import { useEffect, useRef } from "react"
import { ColorType, createChart } from "lightweight-charts"

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
  height: 300,
}

export default function CandleChart(props) {
  const chartContainerRef = useRef()
  useEffect(() => {
    const chart = createChart(chartContainerRef.current, {
      ...options,
      width: chartContainerRef.current.clientWidth,
    })
    const candlestickSeries = chart.addCandlestickSeries({
      upColor: "#3FE8BC",
      downColor: "#D63A5E",
      borderVisible: false,
      wickUpColor: "#3FE8BC",
      wickDownColor: "#D63A5E",
      title: "$",
    })
    const mapped = props.series.map((candle) => {
      return {
        time: Math.round(new Date(candle.bucket).getTime() / 1000),
        open: candle.open,
        high: candle.high,
        low: candle.low,
        close: candle.close,
      }
    })
    candlestickSeries.setData(mapped)
    chart.timeScale().fitContent()
    const handleResize = () => {
      chart.applyOptions({
        ...options,
        width: chartContainerRef.current.clientWidth,
      })
    }
    window.addEventListener("resize", handleResize)
    chart.timeScale().fitContent()
    return () => {
      window.removeEventListener("resize", handleResize)
      chart.remove()
    }
  }, [props.series, props.active])

  return (
    <div className="relative flex-grow" ref={chartContainerRef}>
      <article className="format format-invert absolute left-0 top-0 z-50">
        <div className="text-3xl font-bold text-white">
          {props.symbol} - {props.companyName}
        </div>
      </article>
    </div>
  )
}
