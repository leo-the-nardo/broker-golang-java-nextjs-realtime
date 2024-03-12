"use client";

import { useCurrentRoles } from "@/hooks/use-current-role";
import { FormError } from "@/components/form-error";

interface RoleGateProps {
  children: React.ReactNode;
  allowedRoles: string[];
}

export function RoleGate({ children, allowedRoles }: RoleGateProps) {
  const roles = useCurrentRoles();
  if (!roles!.some((role) => allowedRoles.includes(role)))
    return (
      <FormError message="You don't have permission to view this content" />
    );
  return <>{children}</>;
}
