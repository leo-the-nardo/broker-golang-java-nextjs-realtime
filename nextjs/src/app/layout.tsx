import type { Metadata } from "next"
import { Inter } from "next/font/google"
import "./globals.css"
import DefaultNavbar from "@/app/components/Navbar"

export const metadata: Metadata = {
  title: "Create Next App",
  description: "Generated by create next app",
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en" className="dark">
      <body className="h-screen bg-gray-900">
        <DefaultNavbar />
        {children}
      </body>
    </html>
  )
}