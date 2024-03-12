import * as React from "react"

import { cn } from "@/lib/utils"
import { CSSProperties } from "react"

const CustomCard = React.forwardRef<
  HTMLDivElement,
  React.HTMLAttributes<HTMLDivElement>
>(({ className, ...props }, ref) => (
  <div
    ref={ref}
    style={
      {
        // "--background": "30 41 59",
        "--highlight": "255 255 255",
        "--bg-color":
          "linear-gradient(145deg, rgba(12, 117, 125, 1) 0%, rgba(59, 30, 61, 0.4) 100%)",
        // "linear-gradient(145deg, rgba(59, 30, 61, 1) 0%, rgba(12, 117, 125, 0.1) 100%)",
        "--border-color": `linear-gradient(145deg,rgb(var(--highlight)) 0%, rgb(var(--highlight) / 0.3) 33.33%,rgb(var(--highlight) / 0.14) 66.67%, rgb(var(--highlight) / 0.1) 100%)`,
      } as CSSProperties
    }
    className={cn(
      "rounded-xl border border-transparent [background:padding-box_var(--bg-color),border-box_var(--border-color)]",
      className,
    )}
    {...props}
  />
))

CustomCard.displayName = "CustomCard"

const CardHeader = React.forwardRef<
  HTMLDivElement,
  React.HTMLAttributes<HTMLDivElement>
>(({ className, ...props }, ref) => (
  <div
    ref={ref}
    className={cn("flex flex-col space-y-1.5 p-6", className)}
    {...props}
  />
))
CardHeader.displayName = "CardHeader"

const CardTitle = React.forwardRef<
  HTMLParagraphElement,
  React.HTMLAttributes<HTMLHeadingElement>
>(({ className, ...props }, ref) => (
  <h3
    ref={ref}
    className={cn("font-semibold leading-none tracking-tight", className)}
    {...props}
  />
))
CardTitle.displayName = "CardTitle"

const CardDescription = React.forwardRef<
  HTMLParagraphElement,
  React.HTMLAttributes<HTMLParagraphElement>
>(({ className, ...props }, ref) => (
  <p
    ref={ref}
    className={cn("text-sm text-muted-foreground", className)}
    {...props}
  />
))
CardDescription.displayName = "CardDescription"

const CardContent = React.forwardRef<
  HTMLDivElement,
  React.HTMLAttributes<HTMLDivElement>
>(({ className, ...props }, ref) => (
  <div ref={ref} className={cn("p-6 pt-0", className)} {...props} />
))
CardContent.displayName = "CardContent"

const CardFooter = React.forwardRef<
  HTMLDivElement,
  React.HTMLAttributes<HTMLDivElement>
>(({ className, ...props }, ref) => (
  <div
    ref={ref}
    className={cn("flex items-center p-6 pt-0", className)}
    {...props}
  />
))
CardFooter.displayName = "CardFooter"

export {
  CustomCard,
  CardHeader,
  CardFooter,
  CardTitle,
  CardDescription,
  CardContent,
}
