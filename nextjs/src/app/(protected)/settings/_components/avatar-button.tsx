import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import React from "react"
import { RiEdit2Fill } from "react-icons/ri"
import { useCurrentUser } from "@/hooks/use-current-user"

export default function AvatarButton() {
  const { user } = useCurrentUser()
  return (
    <div className="relative">
      <Avatar className="flex h-12 w-[3.25rem] flex-shrink-0 select-none items-center justify-center text-white">
        <AvatarImage src={user!.picture || ""} />
        <AvatarFallback className=" font-medium text-foreground">
          {user!.name
            .split(" ")
            .map((n) => n[0])
            .join(".")
            .toUpperCase()}
        </AvatarFallback>
        <div className="absolute inset-0 flex items-end justify-center bg-black bg-opacity-30 pb-2 text-white opacity-0 transition-opacity duration-300 hover:opacity-100">
          <RiEdit2Fill className="w-6 animate-bounce" />
        </div>
      </Avatar>
    </div>
  )
}
