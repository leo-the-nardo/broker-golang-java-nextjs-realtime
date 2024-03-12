import { ExtendedUser } from "@/next-auth";
import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";

interface UserInfoProps {
  user?: ExtendedUser;
  label: string;
}

export function UserInfo({ user, label }: UserInfoProps) {
  return (
    <Card className=" w-full max-w-[600px] shadow-md mx-auto overflow-hidden">
      <CardHeader>
        <p className="text-2xl font-semibold text-center">{label}</p>
      </CardHeader>
      <CardContent className="space-y-4">
        <div className="flex flex-row items-center justify-between rounded-lg border p-3 shadow-sm">
          <p className="text-sm font-medium">ID</p>
          <p className="truncate text-xs max-w-[180px] font-mono p-1 bg-slate-100 rounded-md">
            {user?.id}
          </p>
        </div>
        <div className="flex flex-row items-center justify-between rounded-lg border p-3 shadow-sm">
          <p className="text-sm font-medium">Name</p>
          <p className="truncate text-xs max-w-[180px] font-mono p-1 bg-slate-100 rounded-md">
            {user?.name}
          </p>
        </div>
        <div className="flex flex-row items-center justify-between rounded-lg border p-3 shadow-sm">
          <p className="text-sm font-medium">Email</p>
          <p className="truncate text-xs max-w-[180px] font-mono p-1 bg-slate-100 rounded-md">
            {user?.username}
          </p>
        </div>
        <div className="flex flex-row items-center justify-between rounded-lg border p-3 shadow-sm">
          <p className="text-sm font-medium">Roles</p>
          {user?.roles.map((role) => (
            <p
              key={role}
              className="truncate text-xs max-w-[180px] font-mono p-1 bg-slate-100 rounded-md"
            >
              {role}
            </p>
          ))}
        </div>

        <div className="flex flex-row items-center justify-between rounded-lg border p-3 shadow-sm">
          <p className="text-sm font-medium">Two Factor Authentication</p>
          <Badge variant={user?.twoFactorEnabledAt ? "success" : "destructive"}>
            {user?.twoFactorEnabledAt ? "ON" : "OFF"}
          </Badge>
        </div>
        <pre>
          <code className="text-sm">{JSON.stringify(user, null, 2)}</code>
        </pre>
      </CardContent>
    </Card>
  );
}
