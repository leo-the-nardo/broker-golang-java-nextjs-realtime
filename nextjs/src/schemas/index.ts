import * as z from "zod"

export const LoginSchema = z.object({
  email: z.string().email({
    message: "Email is required",
  }),
  password: z.string().min(1, {
    message: "Password is required",
  }),
  code: z.optional(z.string()),
})
export const RegisterSchema = z.object({
  email: z.string().email({
    message: "Email is required",
  }),
  password: z.string().min(6, {
    message: "Minimum 6 characters is required",
  }),
  name: z.string().min(1, {
    message: "Name is required",
  }),
  cpfCnpj: z
    .string({
      required_error: "CPF/CNPJ é obrigatório.",
    })
    .refine((doc) => {
      const replacedDoc = doc.replace(/\D/g, "")
      return replacedDoc.length >= 11
    }, "CPF/CNPJ deve conter no mínimo 11 caracteres.")
    .refine((doc) => {
      const replacedDoc = doc.replace(/\D/g, "")
      return replacedDoc.length <= 14
    }, "CPF/CNPJ deve conter no máximo 14 caracteres.")
    .refine((doc) => {
      const replacedDoc = doc.replace(/\D/g, "")
      return !!Number(replacedDoc)
    }, "CPF/CNPJ deve conter apenas números."),
})
export const ResetSchema = z.object({
  email: z.string().email({
    message: "Email is required",
  }),
})
export const NewPasswordSchema = z.object({
  password: z.string().min(6, {
    message: "Minimum 6 characters is required",
  }),
})

export const ProfileSchema = z.object({
  name: z.string().optional(),
  cpf: z
    .string({
      required_error: "CPF/CNPJ é obrigatório.",
    })
    .refine((doc) => {
      const replacedDoc = doc.replace(/\D/g, "")
      return replacedDoc.length >= 11
    }, "CPF/CNPJ deve conter no mínimo 11 caracteres.")
    .refine((doc) => {
      const replacedDoc = doc.replace(/\D/g, "")
      return replacedDoc.length <= 14
    }, "CPF/CNPJ deve conter no máximo 14 caracteres.")
    .refine((doc) => {
      const replacedDoc = doc.replace(/\D/g, "")
      return !!Number(replacedDoc)
    }, "CPF/CNPJ deve conter apenas números."),
  gender: z.string(),
  birth: z.string(),
  phone: z.string(),
  cep: z.string(),
  street: z.string(),
  number: z.string(),
  complement: z.string(),
  district: z.string(),
  city: z.string(),
  state: z.string(),
})

export const OrderSchema = z.object({
  price: z.number().min(0.01),
  shares: z.coerce.number().min(1),
})
