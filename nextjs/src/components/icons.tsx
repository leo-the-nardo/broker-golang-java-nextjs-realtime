import { FaGithub, FaTwitter } from "react-icons/fa"
import {
  AiOutlineClose,
  AiOutlineArrowRight,
  AiOutlineWarning,
  AiOutlineSetting,
  AiOutlineUser,
} from "react-icons/ai"
import {
  RiLoader2Line,
  RiDeleteBinLine,
  RiFileTextLine,
  RiFileLine,
  RiImageLine,
  RiMore2Line,
  RiAddLine,
} from "react-icons/ri"
import {
  BsChevronLeft,
  BsChevronRight,
  BsMoon,
  BsSun,
  BsLaptop,
  BsFillPersonVcardFill,
  BsPersonLinesFill,
  BsFillTelephoneFill,
} from "react-icons/bs"
import { IoMdHelpCircle } from "react-icons/io"
import { HiArrowUp, HiOutlineDocumentDuplicate } from "react-icons/hi"
import { IconType } from "react-icons"
import {
  IoKeyOutline,
  IoLockClosedOutline,
  IoMailOutline,
} from "react-icons/io5"
import { HiOutlineSquares2X2, HiShoppingCart } from "react-icons/hi2"
import { MdDevices, MdEmail } from "react-icons/md"
import { FaLocationPin, FaUnlock, FaWallet } from "react-icons/fa6"
import { FiActivity, FiBarChart2 } from "react-icons/fi"

export type Icon = IconType

export const Icons = {
  logo: FaGithub,
  close: AiOutlineClose,
  spinner: RiLoader2Line,
  chevronLeft: BsChevronLeft,
  chevronRight: BsChevronRight,
  trash: RiDeleteBinLine,
  post: RiFileTextLine,
  page: RiFileLine,
  media: RiImageLine,
  settings: AiOutlineSetting,
  billing: HiOutlineDocumentDuplicate,
  ellipsis: RiMore2Line,
  add: RiAddLine,
  warning: AiOutlineWarning,
  user: AiOutlineUser,
  arrowRight: AiOutlineArrowRight,
  help: IoMdHelpCircle,
  sun: BsSun,
  moon: BsMoon,
  laptop: BsLaptop,
  gitHub: FaGithub,
  twitter: FaTwitter,
  check: HiOutlineDocumentDuplicate,
  key: IoKeyOutline,
  general: HiOutlineSquares2X2,
  mail: MdEmail,
  mailOut: IoMailOutline,
  lock: FaUnlock,
  lockOut: IoLockClosedOutline,
  identity: BsFillPersonVcardFill,
  personLines: BsPersonLinesFill,
  telephone: BsFillTelephoneFill,
  location: FaLocationPin,
  devices: MdDevices,
  wallet: FaWallet,
  buy: HiShoppingCart,
  sell: HiArrowUp,
  areaChart: FiActivity,
  candleChart: FiBarChart2,
}
