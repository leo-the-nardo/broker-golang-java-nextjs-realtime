"use client"

import Link from "next/link"
import { usePathname } from "next/navigation"
import { cn } from "@/lib/utils"
import { Icons } from "@/components/icons"

export type NavItem = {
  title: string
  href: string
  disabled?: boolean
}

export type MainNavItem = NavItem
export type MarketingConfig = {
  mainNav: MainNavItem[]
}
export type SidebarNavItem = {
  title: string
  disabled?: boolean
  external?: boolean
  icon?: keyof typeof Icons
} & (
  | {
      href: string
      items?: never
    }
  | {
      href?: string
      items: NavItem[]
    }
)

interface DashboardNavProps {
  items: SidebarNavItem[]
}

export function DashboardNav({ items }: DashboardNavProps) {
  const path = usePathname()

  if (!items?.length) {
    return null
  }

  return (
    <nav className="grid w-full grid-flow-col items-center justify-evenly border-b  md:grid-flow-row md:items-start md:justify-stretch md:gap-2 md:border-0">
      {items.map((item, index) => {
        const Icon = Icons[item.icon || "arrowRight"]
        return (
          item.href && (
            <Link key={index} href={item.disabled ? "/" : item.href}>
              <span
                className={cn(
                  "group flex items-center text-sm font-medium hover:text-accent-foreground md:px-6 md:py-3.5 md:hover:bg-accent",
                  path === item.href
                    ? "border-b-2 border-b-accent-foreground font-medium text-accent-foreground md:border-b-0 md:border-l md:border-l-accent-foreground md:bg-background"
                    : "transparent font-normal text-foreground/75",
                  item.disabled && "cursor-not-allowed opacity-80",
                )}
              >
                <Icon
                  className={cn(
                    // path === item.href
                    //   ? "text-accent-foreground"
                    //   : "text-foreground/80",
                    "h-5 w-5 md:mr-2",
                  )}
                />
                <span className="mr-auto hidden text-base md:block">
                  {item.title}
                </span>
                <Icons.chevronRight className="hidden h-3 w-3 md:block" />
              </span>
            </Link>
          )
        )
      })}
    </nav>
  )
}
