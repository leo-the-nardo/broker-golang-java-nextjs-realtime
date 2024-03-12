"use client";

import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { useCurrentUser } from "@/hooks/use-current-user";
import { LogoutButton } from "@/components/auth/logout-button";
import { ExitIcon } from "@radix-ui/react-icons";
import React from "react";
import Link from "next/link";
import { Icons } from "@/components/icons";

export function UserButton() {
  const { user } = useCurrentUser();
  return (
    <DropdownMenu>
      <DropdownMenuTrigger>
        <Avatar>
          <AvatarImage src={user?.picture || ""} />
          <AvatarFallback className="text-foreground font-medium">
            {user?.name
              .split(" ")
              .map((n) => n[0])
              .join(".")
              .toUpperCase()}
            {/*  Edit icon */}
          </AvatarFallback>
        </Avatar>
      </DropdownMenuTrigger>
      <DropdownMenuContent className="w-40 shadow-md" align="end">
        <DropdownMenuItem className="cursor-pointer" asChild>
          <Link href="/settings">
            <Icons.settings className="h-4 w-4 mr-4" />
            Settings
          </Link>
        </DropdownMenuItem>
        <DropdownMenuSeparator />
        <LogoutButton>
          <DropdownMenuItem className="cursor-pointer">
            <ExitIcon className="h-4 w-4 mr-4" />
            Logout
          </DropdownMenuItem>
        </LogoutButton>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}
