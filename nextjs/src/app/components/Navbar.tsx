"use client"
import {
  Navbar,
  NavbarBrand,
  NavbarCollapse,
  NavbarLink,
  NavbarToggle,
} from "flowbite-react"
import { usePathname, useParams } from "next/navigation"
import Link from "next/link"
import Image from "next/image"

export default function DefaultNavbar() {
  const pathname = usePathname()
  const params = useParams()

  return (
    <Navbar fluid rounded>
      <NavbarBrand href="">
        <Image
          className="mr-3 h-6 sm:h-9"
          alt="Leozin Invest"
          src="https://github.com/leo-the-nardo.png"
          width={37}
          height={40}
        />
        <span className="self-center whitespace-nowrap text-xl font-semibold dark:text-white">
          Leo-the-nardo Invest
        </span>
      </NavbarBrand>
      <NavbarToggle />
      <NavbarCollapse>
        <NavbarLink
          active={pathname === `/${params.wallet_id}`}
          as={Link}
          href={`/${params.wallet_id}`}
        >
          Home
        </NavbarLink>
        <NavbarLink href="#">Ativos</NavbarLink>
      </NavbarCollapse>
      <div className="flex text-white md:order-2">Olá {params.wallet_id}</div>
    </Navbar>
  )
}
