//package cn.edu.nju.charlesfeng.util;
//
//import cn.edu.nju.charlesfeng.filter.Seat;
//import cn.edu.nju.charlesfeng.model.Order;
//
//import java.util.LinkedList;
//import java.util.List;
//
//public class OrderSeatHelper {
//
//    /**
//     * 获得座位的ID
//     */
//    public static List<String> getSeatListIds(List<Seat> seats) {
//        List<String> ids = new LinkedList<>();
//        for (Seat seat : seats) {
//            ids.add(seat.getId());
//        }
//        return ids;
//    }
//
//    /**
//     * 替换指定位置的座位为大写（座位锁定）
//     */
//    public static String orderSpecificSeat(String str, int pos) {
//        StringBuilder sb = new StringBuilder();
//        sb.append(str.substring(0, pos));
//        sb.append((char) (str.charAt(pos) - 0x20));
//        if (pos < str.length() - 1) sb.append(str.substring(pos + 1));
//        return sb.toString();
//    }
//
//    /**
//     * 替换指定位置的座位为小写（座位释放）
//     */
//    public static String releaseSpecificSeat(String str, int pos) {
//        StringBuilder sb = new StringBuilder();
//        sb.append(str.substring(0, pos));
//        sb.append((char) (str.charAt(pos) + 0x20));
//        if (pos < str.length() - 1) sb.append(str.substring(pos + 1));
//        return sb.toString();
//    }
//
//    /**
//     * @param order 要释放座位的订单
//     * @return 释放座位后的订单，使订单可以级联更新其中的座位
//     */
//    public static Order releaseSeatsInOrder(Order order) {
////        if (order.getOrderedSeatsJson() != null) {
////            // 需要释放座位
////            List<Seat> curOrderedSeats = JSON.parseArray(order.getOrderedSeatsJson(), Seat.class);
////            List<String> curOrderedSeatIds = OrderSeatHelper.getSeatListIds(curOrderedSeats);
////
////            Schedule toUpdateSchedule = order.getSchedule();
////            List<String> alreadyBookedIds = JSON.parseArray(toUpdateSchedule.getBookedSeatsIdJson(), String.class);
////            alreadyBookedIds.removeAll(curOrderedSeatIds);
////            toUpdateSchedule.setBookedSeatsIdJson(JSON.toJSONString(alreadyBookedIds));
////
////            List<String> curRemainSeatMap = JSON.parseArray(toUpdateSchedule.getRemainSeatsJson(), String.class);
////            for (String id : curOrderedSeatIds) {
////                String[] idParts = id.split("_");
////                int rowIndex = Integer.parseInt(idParts[0]) - 1;
////                int colIndex = Integer.parseInt(idParts[1]) - 1;
////
////                curRemainSeatMap.set(rowIndex,
////                        OrderSeatHelper.releaseSpecificSeat(curRemainSeatMap.get(rowIndex), colIndex));
////            }
////            toUpdateSchedule.setRemainSeatsJson(JSON.toJSONString(curRemainSeatMap));
////        }
////        return order;
//        return null;
//    }
//}
