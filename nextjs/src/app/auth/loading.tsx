import { Skeleton } from "@/components/ui/skeleton";
import { Card } from "@/components/ui/card";

export default function AuthLoading() {
  return (
    <Card className="w-full max-w-[400px] shadow-md h-[60vh] p-8 md:p-8 overflow-hidden my-auto md:my-0">
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
    </Card>
  );
}
