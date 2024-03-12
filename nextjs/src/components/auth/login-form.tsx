"use client";
import * as z from "zod";
import { CardWrapper } from "@/components/auth/card-wrapper";
import { useForm } from "react-hook-form";
import { LoginSchema } from "@/schemas";
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
import { login } from "@/actions/login";
import { useState, useTransition } from "react";
import Link from "next/link";
import { LoadingButton } from "@/components/ui/button2";

export function LoginForm() {
  const [showTwoFactor, setShowTwoFactor] = useState<boolean | undefined>(
    false,
  );
  const [error, setError] = useState<string | undefined>("");
  const [success, setSuccess] = useState<string | undefined>("");
  const form = useForm<z.infer<typeof LoginSchema>>({
    resolver: zodResolver(LoginSchema),
    defaultValues: {
      email: "",
      password: "",
    },
  });
  const [isPending, startTransition] = useTransition();

  const onSubmit = (data: z.infer<typeof LoginSchema>) => {
    setError(""); // reset
    setSuccess("");

    startTransition(() => {
      // server action
      login(data)
        .then((data) => {
          if (!data) {
            setError("");
            setSuccess("");
            setShowTwoFactor(false);
            return;
          }
          setSuccess(data.success);
          setError(data.error);
          setShowTwoFactor(data.twoFactor);
          if (data.twoFactor) {
            setShowTwoFactor(true);
          }
        })
        .catch((err) => {
          console.error(err);
          setError("Something went wrong!");
        });
    });
  };

  return (
    <CardWrapper
      headerLabel="Welcome back"
      backButtonLabel="Don't have an account"
      backButtonHref="/auth/register"
      showSocial
    >
      <Form {...form}>
        <form className="space-y-6" onSubmit={form.handleSubmit(onSubmit)}>
          <div className="space-y-4">
            {showTwoFactor && (
              <FormField
                control={form.control}
                name="code"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Two Factor Code</FormLabel>
                    <FormControl>
                      <Input
                        {...field}
                        disabled={isPending}
                        placeholder="123456"
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            )}
            {!showTwoFactor && (
              <>
                <FormField
                  control={form.control}
                  name="email"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Email</FormLabel>
                      <FormControl>
                        <Input
                          {...field}
                          disabled={isPending}
                          placeholder="john.doe@gmail.com"
                          type="email"
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
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
                          placeholder="********"
                          type="password"
                        />
                      </FormControl>
                      <Button
                        variant="link"
                        size="sm"
                        asChild
                        className="px-0 font-normal
                 "
                      >
                        <Link href="/auth/reset">Forgot password?</Link>
                      </Button>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              </>
            )}
          </div>
          <FormSuccess message={success} />
          <FormError message={error} />
          <LoadingButton
            loading={isPending}
            type="submit"
            className="md:w-full"
          >
            {showTwoFactor ? "Verify" : "Login"}
          </LoadingButton>
        </form>
      </Form>
    </CardWrapper>
  );
}
