"use client"

import { useCurrentUser } from "@/hooks/use-current-user"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"

export default function AvatarC() {
  const { user } = useCurrentUser()
  return (
    <Avatar>
      <AvatarImage src={user?.picture || ""} />
      <AvatarFallback className="font-medium text-foreground">
        {user?.name
          .split(" ")
          .map((n) => n[0])
          .join(".")
          .toUpperCase()}
        {/*  Edit icon */}
      </AvatarFallback>
    </Avatar>
  )
}
