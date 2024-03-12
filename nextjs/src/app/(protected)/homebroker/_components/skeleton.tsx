"use client"
import { Skeleton } from "@/components/ui/skeleton"
import { cn } from "@/lib/utils"

export default function SkeletonC({ className }: { className?: string }) {
  return (
    <div className={cn(className, "overflow-hidden transition-all")}>
      <div className="h-full w-full p-8">
        <div className="grid grid-cols-1  gap-6 md:gap-16">
          <div className="flex flex-col gap-6 md:gap-10">
            <Skeleton className="h-16 w-full" />
            <div className="flex flex-col gap-4">
              <Skeleton className="h-4 w-full" />
              <Skeleton className="h-4 w-8/12 " />
              <Skeleton className="h-4 w-full" />
              <Skeleton className="h-4 w-4/12 " />
            </div>
            <Skeleton className="h-16 w-full md:w-80" />
            <div className="flex flex-col gap-4">
              <Skeleton className="h-4 w-full" />
              <Skeleton className="h-4 w-8/12 " />
              <Skeleton className="h-4 w-full" />
              <Skeleton className="h-4 w-4/12 " />
            </div>{" "}
            <Skeleton className="h-16 w-full md:w-80" />
            <div className="flex flex-col gap-4">
              <Skeleton className="h-4 w-full" />
              <Skeleton className="h-4 w-8/12 " />
              <Skeleton className="h-4 w-full" />
              <Skeleton className="h-4 w-4/12 " />
            </div>
          </div>
          <div className="flex flex-col gap-6 md:gap-10">
            <Skeleton className="h-16 w-full" />
            <div className="flex flex-col gap-4">
              <Skeleton className="h-4 w-full" />
              <Skeleton className="h-4 w-8/12 " />
              <Skeleton className="h-4 w-full" />
              <Skeleton className="h-4 w-4/12 " />
            </div>
            <Skeleton className="h-16 w-full md:w-80" />
            <div className="flex flex-col gap-4">
              <Skeleton className="h-4 w-full" />
              <Skeleton className="h-4 w-8/12 " />
              <Skeleton className="h-4 w-full" />
              <Skeleton className="h-4 w-4/12 " />
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
