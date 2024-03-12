"use client";
import * as z from "zod";
import { CardWrapper } from "@/components/auth/card-wrapper";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { FormError } from "@/components/form-error";
import { FormSuccess } from "@/components/form-success";
import { useState, useTransition } from "react";
import { NewPasswordSchema } from "@/schemas";
import { useSearchParams } from "next/navigation";
import { newPassword } from "@/actions/new-password";
import { LoadingButton } from "@/components/ui/button2";

export default function NewPasswordForm() {
  const searchParams = useSearchParams();
  const token = searchParams.get("token");
  const [error, setError] = useState<string | undefined>("");
  const [success, setSuccess] = useState<string | undefined>("");
  const form = useForm<z.infer<typeof NewPasswordSchema>>({
    resolver: zodResolver(NewPasswordSchema),
    defaultValues: {
      password: "",
    },
  });
  const [isPending, startTransition] = useTransition();

  const onSubmit = (data: z.infer<typeof NewPasswordSchema>) => {
    setError(""); // reset
    setSuccess("");

    console.log(data);

    startTransition(() => {
      newPassword(data, token!).then((data) => {
        if (!data) {
          setError("");
          setSuccess("");
          return;
        }
        setSuccess(data.success);
        setError(data.error);
      });
    });
  };

  return (
    <CardWrapper
      headerLabel="Enter a new password"
      backButtonLabel="Back to Login"
      backButtonHref="/auth/login"
    >
      <Form {...form}>
        <form className="space-y-6" onSubmit={form.handleSubmit(onSubmit)}>
          <div className="space-y-4">
            <FormField
              control={form.control}
              name="password"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Password</FormLabel>
                  <FormControl>
                    <Input
                      {...field}
                      disabled={isPending}
                      placeholder="*******"
                      type="password"
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>
          <FormSuccess message={success} />
          <FormError message={error} />
          <LoadingButton
            loading={isPending}
            type="submit"
            className="w-full md:w-full"
          >
            Reset password
          </LoadingButton>
        </form>
      </Form>
    </CardWrapper>
  );
}
