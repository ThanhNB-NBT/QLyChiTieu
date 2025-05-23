package com.nhom08.qlychitieu.tien_ich;

import android.util.Log;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * CalculatorHandler - Lớp xử lý tính toán cho máy tính đơn giản trong ứng dụng
 * Được cập nhật vào: 2025-05-23 bởi ThanhNB-NBT
 * Class này cung cấp các chức năng cơ bản cho một máy tính:
 * - Nhập số và toán tử
 * - Thực hiện các phép tính cộng trừ
 * - Format kết quả theo định dạng số
 */
public class CalculatorHandler {
    // TAG để sử dụng cho logging
    private static final String TAG = CalculatorHandler.class.getSimpleName();

    // Các ký tự đặc biệt trong phép tính
    private static final String THOUSAND_SEPARATOR = "000"; // Phím tắt thêm 3 số 0
    private static final String HUNDRED_SEPARATOR = "00";  // Phím tắt thêm 2 số 0
    private static final String OPERATORS = "+-";          // Các toán tử được hỗ trợ

    // StringBuilder để lưu trữ và xử lý biểu thức
    private final StringBuilder input;

    // DecimalFormat để format số theo định dạng mong muốn
    private final DecimalFormat decimalFormat;

    /**
     * Constructor - Khởi tạo CalculatorHandler
     * Thiết lập StringBuilder để lưu biểu thức và DecimalFormat để format kết quả
     */
    public CalculatorHandler() {
        input = new StringBuilder();
        // Khởi tạo DecimalFormat với dấu chấm làm dấu phân cách thập phân
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator('.');
        decimalFormat = new DecimalFormat("#.##", symbols);
    }

    /**
     * Thêm một số vào biểu thức
     *
     * @param number Số cần thêm vào biểu thức (dạng String)
     */
    public void appendNumber(String number) {
        // Chỉ kiểm tra xem đầu vào có phải là số đơn lẻ (0-9)
        if (number != null && number.matches("\\d")) {
            input.append(number);
            Log.d(TAG, "Số sau khi thêm: " + input.toString());
        }
    }

    /**
     * Thêm toán tử vào biểu thức
     * Chỉ thêm nếu chưa có toán tử hoặc thay thế toán tử hiện tại
     *
     * @param operator Toán tử cần thêm (+ hoặc -)
     */
    public void appendOperator(String operator) {
        if (!isValidOperator(operator)) return;

        String currentInput = input.toString();

        // Nếu biểu thức rỗng và là dấu trừ (cho phép số âm)
        if (currentInput.isEmpty() && operator.equals("-")) {
            input.append(operator);
            Log.d(TAG, "Thêm dấu trừ đầu tiên: " + input.toString());
            return;
        }

        // Nếu biểu thức rỗng và là dấu cộng, bỏ qua
        if (currentInput.isEmpty() && operator.equals("+")) {
            return;
        }

        // Nếu ký tự cuối là toán tử, thay thế nó
        if (!currentInput.isEmpty()) {
            char lastChar = currentInput.charAt(currentInput.length() - 1);

            if (OPERATORS.indexOf(lastChar) != -1) {
                input.setCharAt(input.length() - 1, operator.charAt(0));
                Log.d(TAG, "Thay thế toán tử: " + input.toString());
                return;
            }

            // Chỉ thêm toán tử nếu ký tự cuối không phải là toán tử
            input.append(operator);
            Log.d(TAG, "Thêm toán tử mới: " + input.toString());
        }
    }

    /**
     * Thêm ba số 0 vào số hiện tại
     * 1. Nếu chuỗi rỗng, thêm một số 0
     * 2. Nếu kết thúc bằng toán tử, thêm một số 0
     * 3. Nếu không, thêm ba số 0
     */
    public void append000() {
        // Trường hợp 1: Nếu chuỗi rỗng, thêm một số 0
        if (input.length() == 0) {
            input.append("0");
            Log.d(TAG, "Thêm số 0 vào chuỗi rỗng: " + input.toString());
            return;
        }

        // Trường hợp 2: Nếu kết thúc bằng toán tử, thêm một số 0
        if (endsWithOperator()) {
            input.append("0");
            Log.d(TAG, "Thêm số 0 sau toán tử: " + input.toString());
            return;
        }

        // Trường hợp 3: Thêm ba số 0
        input.append(THOUSAND_SEPARATOR);
        Log.d(TAG, "Thêm 000: " + input.toString());
    }

    /**
     * Thêm hai số 0 vào số hiện tại
     * 1. Nếu chuỗi rỗng, thêm một số 0
     * 2. Nếu kết thúc bằng toán tử, thêm một số 0
     * 3. Nếu không, thêm hai số 0
     */
    public void append00() {
        // Trường hợp 1: Nếu chuỗi rỗng, thêm một số 0
        if (input.length() == 0) {
            input.append("0");
            Log.d(TAG, "Thêm số 0 vào chuỗi rỗng: " + input.toString());
            return;
        }

        // Trường hợp 2: Nếu kết thúc bằng toán tử, thêm một số 0
        if (endsWithOperator()) {
            input.append("0");
            Log.d(TAG, "Thêm số 0 sau toán tử: " + input.toString());
            return;
        }

        // Trường hợp 3: Thêm hai số 0
        input.append(HUNDRED_SEPARATOR);
        Log.d(TAG, "Thêm 000: " + input.toString());
    }

    /**
     * Xóa toàn bộ biểu thức hiện tại
     */
    public void clear() {
        input.setLength(0);
        Log.d(TAG, "Đã xóa biểu thức");
    }

    /**
     * Lấy biểu thức hiện tại
     *
     * @return Chuỗi biểu thức hiện tại
     */
    public String getCurrentInput() {
        return input.toString();
    }

    /**
     * Tính toán kết quả của biểu thức hiện tại
     *
     * @return Kết quả tính toán
     * @throws NumberFormatException nếu biểu thức không hợp lệ
     */
    public double calculate() throws NumberFormatException {
        String expression = input.toString();
        if (expression.isEmpty()) return 0;

        // Xử lý biểu thức không hoàn chỉnh
        if (expression.equals("+") || expression.equals("-")) {
            return 0;
        }

        // Nếu biểu thức kết thúc bằng toán tử, loại bỏ nó
        if (endsWithOperator()) {
            expression = expression.substring(0, expression.length() - 1);
        }

        try {
            if (containsOperators(expression)) {
                return calculateExpression(expression);
            }
            return Double.parseDouble(expression);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Lỗi tính toán biểu thức: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Lấy kết quả đã được format theo định dạng số
     *
     * @return Chuỗi kết quả đã được format
     */
    public String getFormattedResult() {
        try {
            double result = calculate();
            return decimalFormat.format(result);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Lỗi định dạng kết quả: " + e.getMessage());
            return "0";
        }
    }

    // Các phương thức private helper

    /**
     * Kiểm tra toán tử có hợp lệ không (+ hoặc -)
     */
    private boolean isValidOperator(String operator) {
        return operator != null && (operator.equals("+") || operator.equals("-"));
    }

    /**
     * Lấy số cuối cùng trong biểu thức (phần sau toán tử cuối)
     */
    private String getLastNumber() {
        String expression = input.toString();
        int lastOperatorIndex = Math.max(
                expression.lastIndexOf("+"),
                expression.lastIndexOf("-")
        );

        // Trường hợp đặc biệt: nếu dấu trừ ở đầu (số âm)
        if (lastOperatorIndex == 0 && expression.charAt(0) == '-') {
            return expression;
        }

        return lastOperatorIndex == -1 ?
                expression :
                expression.substring(lastOperatorIndex + 1);
    }

    /**
     * Kiểm tra biểu thức có kết thúc bằng toán tử không
     */
    private boolean endsWithOperator() {
        if (input.length() == 0) return false;
        char lastChar = input.charAt(input.length() - 1);
        return OPERATORS.indexOf(lastChar) != -1;
    }

    /**
     * Kiểm tra biểu thức có chứa toán tử không,
     * bỏ qua dấu trừ đầu tiên (số âm)
     */
    private boolean containsOperators(String expression) {
        // Đối với chuỗi bắt đầu bằng dấu trừ (số âm)
        if (expression.startsWith("-")) {
            return expression.substring(1).contains("+") ||
                    expression.substring(1).contains("-");
        }
        return expression.contains("+") || expression.contains("-");
    }

    /**
     * Tính toán kết quả của biểu thức có chứa toán tử
     */
    private double calculateExpression(String expression) {
        // Xử lý số âm ở đầu biểu thức
        boolean startsWithNegative = expression.startsWith("-");
        if (startsWithNegative) {
            expression = "0" + expression;
        }

        // Tách biểu thức thành các phần số
        String[] parts = expression.split("[+\\-]");

        // Kiểm tra mảng parts có hợp lệ không
        if (parts.length == 0) return 0;

        // Bỏ qua phần tử đầu tiên rỗng nếu biểu thức bắt đầu bằng toán tử
        int startIndex = parts[0].isEmpty() ? 1 : 0;

        // Bắt đầu với số đầu tiên
        double result = startIndex < parts.length ? Double.parseDouble(parts[startIndex]) : 0;
        int partIndex = startIndex + 1;

        // Duyệt qua từng ký tự trong biểu thức để tìm toán tử
        for (int i = 0; i < expression.length(); i++) {
            char operator = expression.charAt(i);

            // Xử lý toán tử cộng
            if (operator == '+') {
                if (i > 0 && expression.charAt(i-1) != '-' && expression.charAt(i-1) != '+') {
                    if (partIndex < parts.length) {
                        result += Double.parseDouble(parts[partIndex++]);
                    }
                }
            }
            // Xử lý toán tử trừ
            else if (operator == '-') {
                if (i > 0 && expression.charAt(i-1) != '+' && expression.charAt(i-1) != '-') {
                    if (partIndex < parts.length) {
                        result -= Double.parseDouble(parts[partIndex++]);
                    }
                }
            }
        }

        return result;
    }
}