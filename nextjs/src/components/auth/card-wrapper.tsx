"use client"

import { Card, CardContent, CardHeader, CardFooter } from "@/components/ui/card"
import { Header } from "@/components/auth/header"
import { Social } from "@/components/auth/social"
import { BackButton } from "@/components/auth/back-button"
import { CustomCard } from "@/components/ui/custom-card"

interface CardWrapperProps {
  children: React.ReactNode
  headerLabel: string
  backButtonLabel: string
  backButtonHref: string
  showSocial?: boolean
}

export function CardWrapper({
  children,
  backButtonHref,
  backButtonLabel,
  headerLabel,
  showSocial,
}: CardWrapperProps) {
  return (
    <CustomCard className="w-full max-w-[400px] shadow-md md:w-[400px]">
      <CardHeader>
        <Header label={headerLabel} />
      </CardHeader>
      <CardContent>{children}</CardContent>
      <CardFooter>{showSocial && <Social />}</CardFooter>
      <CardFooter>
        <BackButton label={backButtonLabel} href={backButtonHref} />
      </CardFooter>
    </CustomCard>
  )
}
