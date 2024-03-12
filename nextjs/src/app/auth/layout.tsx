export default function AuthLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="items-center flex-col justify-center flex h-full">
      {children}
    </div>
  );
}
