import { LoadingButton } from "@/components/ui/button2"
import { cn } from "@/lib/utils"

type PrimaryButtonProps = {
  loading?: boolean
  type?: React.ButtonHTMLAttributes<any>["type"]
  children: React.ReactNode
  className?: string
  onClick?: React.MouseEventHandler<HTMLButtonElement>
  onSumbit?: React.FormEventHandler<HTMLButtonElement>
}
export default function PrimaryButton({
  loading,
  type,
  children,
  className,
}: PrimaryButtonProps) {
  return (
    <LoadingButton
      loading={loading ?? false}
      type={type ?? "button"}
      className={cn(
        "group relative mb-2 inline-flex h-auto  items-center justify-center overflow-hidden rounded-lg bg-gradient-to-br from-green-400 to-cyan-600 p-0.5 text-white shadow  transition-all hover:bg-cyan-400 focus:outline-none focus:ring-4 focus:ring-green-800 group-hover:from-green-400 group-hover:to-blue-600",
        className,
      )}
    >
      <span
        className={cn(
          "relative  w-full rounded-md bg-white px-5 py-2.5 text-xs font-semibold uppercase tracking-wide transition-all duration-75 ease-in group-hover:bg-opacity-0 dark:bg-[#1D2C3A] md:w-auto",
          className,
        )}
      >
        {children}
      </span>
    </LoadingButton>
  )
}
