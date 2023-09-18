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
}
export default function AreaChart(props) {
  const chartContainerRef = useRef()

  useEffect(() => {
    const chart = createChart(chartContainerRef.current, {
      ...options,
      width: chartContainerRef.current.clientWidth,
      height: 300,
    })
    const areaSeries = chart.addAreaSeries({
      title: "$",
      lineColor: "#3FE8BC",
      topColor: "rgba(63, 232, 188, 0.9)",
      bottomColor: "rgba(63, 232, 188, 0.1)",
    })
    const mapped = props.series.map((candle) => {
      return {
        time: Date.parse(candle.time) / 1000,

        value: candle.price,
      }
    })
    areaSeries.setData(mapped)
    chart.timeScale().applyOptions({
      timeVisible: true,
    })
    const handleResize = () => {
      chart.applyOptions({
        ...options,
        width: chartContainerRef.current.clientWidth,
        height: 300,
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
