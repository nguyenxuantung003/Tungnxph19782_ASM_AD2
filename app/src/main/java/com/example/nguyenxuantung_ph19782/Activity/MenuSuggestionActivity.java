package com.example.nguyenxuantung_ph19782.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.nguyenxuantung_ph19782.R;

public class MenuSuggestionActivity extends AppCompatActivity {

    private TextView tvBMIDetails;
    private TextView tvMenuSuggestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_suggestion);

        tvBMIDetails = findViewById(R.id.tvBMIDetails);
        tvMenuSuggestion = findViewById(R.id.tvMenuSuggestion);

        Intent intent = getIntent();
        double bmi = intent.getDoubleExtra("BMI", 0.0);
        tvBMIDetails.setText(String.format("Your BMI: %.2f", bmi));

        // Suggest menu based on BMI value
        String menuSuggestion = String.valueOf(getMenuSuggestion(bmi));
        tvMenuSuggestion.setText(menuSuggestion);
    }

    private SpannableStringBuilder getMenuSuggestion(double bmi) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        if (bmi < 18.5) {
            appendFormattedText(builder, "Chỉ số BMI của bạn nhỏ hơn 18.5 (dưới mức bình thường), việc tăng cân là quan trọng. Đây là một số gợi ý cho thực đơn\n\n", true, false);
            appendFormattedText(builder, "Bữa Sáng\n", true, true);
            appendFormattedText(builder, "Bánh mì nướng với bơ và mứt: Thêm một số hạt và quả khô để cung cấp thêm calo.\n", false, false);
            appendFormattedText(builder, "Sinh tố: Sữa, chuối, bơ đậu phộng, và một ít yến mạch.\n", false, false);
            appendFormattedText(builder, "Trứng và bơ: Trứng chiên hoặc luộc kèm theo bơ và một lát bánh mì nguyên cám.\n\n", false, false);
            appendFormattedText(builder, "Bữa Trưa\n", true, true);
            appendFormattedText(builder, "Cơm gạo lứt hoặc cơm trắng: Kèm theo thịt gà, bò hoặc cá. Thêm rau củ và một phần đậu.\n", false, false);
            appendFormattedText(builder, "Mì hoặc phở: Kèm thịt, rau và một ít đậu hủ.\n\n", false, false);
            appendFormattedText(builder, "Bữa Tối\n", true, true);
            appendFormattedText(builder, "Thịt nướng hoặc xào: Kèm theo khoai tây hoặc khoai lang.\n", false, false);
            appendFormattedText(builder, "Salad: Làm từ rau xanh, hạt và một loại sốt giàu chất béo như sốt dầu ô liu.\n\n", false, false);
            appendFormattedText(builder, "Các Bữa Ăn Nhẹ\n", true, true);
            appendFormattedText(builder, "Hạt và quả khô: Hạnh nhân, óc chó, và nho khô.\n", false, false);
            appendFormattedText(builder, "Sữa chua: Kèm theo mật ong hoặc trái cây tươi.\n", false, false);
            appendFormattedText(builder, "Các loại quả: Chuối, xoài, bơ, và táo.\n\n", false, false);
            appendFormattedText(builder, "Thức Uống\n", true, true);
            appendFormattedText(builder, "Sữa: Cung cấp nhiều calo và protein.\n", false, false);
            appendFormattedText(builder, "Sinh tố trái cây: Thêm sữa chua hoặc sữa để tăng lượng calo.\n\n", false, false);
            appendFormattedText(builder, "Một Số Lưu Ý\n", true, true);
            appendFormattedText(builder, "Tăng cường calo: Hãy chọn những thực phẩm có chứa nhiều calo và chất béo lành mạnh như bơ, các loại hạt, và dầu thực vật.\n", false, false);
            appendFormattedText(builder, "Ăn nhiều bữa nhỏ: Thay vì ba bữa chính lớn, hãy ăn nhiều bữa nhỏ trong ngày.\n", false, false);
            appendFormattedText(builder, "Tập thể dục: Kết hợp với tập thể dục để tăng cơ bắp và cải thiện sức khỏe tổng thể.\n", false, false);
            appendFormattedText(builder, "Nếu có vấn đề về sức khỏe hoặc cần tư vấn cụ thể hơn, bạn nên tham khảo ý kiến của chuyên gia dinh dưỡng.\n", false, false);

        } else if (bmi >= 18.5 && bmi < 25) {
            appendFormattedText(builder, "Chỉ số BMI trong khoảng từ 18.5 đến 24.9, bạn thuộc phạm vi cân nặng bình thường. Mục tiêu chính là duy trì cân nặng và sức khỏe tốt. Dưới đây là gợi ý thực đơn cân bằng để duy trì cân nặng và sức khỏe:\n\n", true, false);
            appendFormattedText(builder, "Bữa Sáng\n", true, true);
            appendFormattedText(builder, "Bánh mì nguyên cám với trứng: Thêm rau xanh như cải bó xôi hoặc cà chua.\n", false, false);
            appendFormattedText(builder, "Sữa chua với trái cây tươi và hạt: Nâng cao giá trị dinh dưỡng bằng cách thêm một ít hạt chia hoặc hạt lanh.\n", false, false);
            appendFormattedText(builder, "Sinh tố xanh: Sữa, rau xanh, chuối và một ít quả mọng.\n\n", false, false);
            appendFormattedText(builder, "Bữa Trưa\n", true, true);
            appendFormattedText(builder, "Cơm gạo lứt hoặc quinoa: Kèm theo thịt nạc như gà hoặc cá, và nhiều rau củ.\n", false, false);
            appendFormattedText(builder, "Salad lớn: Rau xanh, cà chua, dưa chuột, và một nguồn protein như đậu phụ hoặc gà.\n", false, false);
            appendFormattedText(builder, "Mì ống nguyên cám: Kèm sốt cà chua tự làm và một ít thịt hoặc hải sản.\n\n", false, false);
            appendFormattedText(builder, "Bữa Tối\n", true, true);
            appendFormattedText(builder, "Thịt nạc nướng hoặc xào: Kèm theo khoai lang hoặc khoai tây và rau củ.\n", false, false);
            appendFormattedText(builder, "Soup rau củ: Để cung cấp chất xơ và vitamin.\n", false, false);
            appendFormattedText(builder, "Salad trộn: Với sốt dầu ô liu và giấm balsamic.\n\n", false, false);
            appendFormattedText(builder, "Các Bữa Ăn Nhẹ\n", true, true);
            appendFormattedText(builder, "Trái cây tươi: Như táo, lê, hoặc quả mọng.\n", false, false);
            appendFormattedText(builder, "Hạt: Như hạnh nhân, óc chó, hoặc hạt điều.\n", false, false);
            appendFormattedText(builder, "Sữa chua hoặc phô mai ít béo: Có thể kèm theo một ít mật ong hoặc trái cây\n\n", false, false);
            appendFormattedText(builder, "Thức Uống\n", true, true);
            appendFormattedText(builder, "Nước: Đảm bảo uống đủ nước mỗi ngày.\n", false, false);
            appendFormattedText(builder, "Trà xanh hoặc trà thảo mộc: Có thể giúp duy trì sức khỏe và cung cấp các chất chống oxy hóa.\n", false, false);
            appendFormattedText(builder, "Nước ép trái cây tươi: Không thêm đường để duy trì lượng calo hợp lý.\n\n", false, false);
            appendFormattedText(builder, "Một Số Lưu Ý\n", true, true);
            appendFormattedText(builder, "Ăn cân bằng: Đảm bảo mỗi bữa ăn có đủ protein, carbohydrate, và chất béo lành mạnh.\n", false, false);
            appendFormattedText(builder, "Chế độ ăn đa dạng: Bao gồm nhiều loại thực phẩm để cung cấp đủ vitamin và khoáng chất.\n", false, false);
            appendFormattedText(builder, "Tập thể dục đều đặn: Kết hợp các hoạt động thể chất để duy trì sức khỏe tổng thể và cân nặng hợp lý.\n", false, false);

        } else if (bmi >= 25 && bmi < 30) {
            appendFormattedText(builder, "Chỉ số BMI của bạn nằm trong khoảng từ 25 đến 29.9, bạn thuộc nhóm thừa cân. Mục tiêu là giảm cân từ từ và duy trì sức khỏe tốt. Dưới đây là gợi ý thực đơn giúp giảm cân một cách lành mạnh:\n\n", true, false);
            appendFormattedText(builder, "Bữa Sáng\n", true, true);
            appendFormattedText(builder, "Yến mạch: Kèm theo trái cây tươi và một ít hạt chia.\n", false, false);
            appendFormattedText(builder, "Sữa chua ít béo: Với một ít quả mọng và hạt lanh.\n", false, false);
            appendFormattedText(builder, "Trứng luộc: Kèm theo một lát bánh mì nguyên cám và rau xanh.\n\n", false, false);
            appendFormattedText(builder, "Bữa Trưa\n", true, true);
            appendFormattedText(builder, "Salad lớn: Rau xanh, cà chua, dưa chuột, và nguồn protein như ức gà nướng hoặc cá.\n", false, false);
            appendFormattedText(builder, "Cơm gạo lứt hoặc quinoa: Kèm theo một phần nhỏ và nhiều rau củ.\n", false, false);
            appendFormattedText(builder, "Soup rau củ: Giúp cung cấp chất xơ và vitamin mà không thêm nhiều calo.\n\n", false, false);
            appendFormattedText(builder, "Bữa Tối\n", true, true);
            appendFormattedText(builder, "Thịt nạc hoặc cá: Kèm theo rau hấp hoặc nướng.\n", false, false);
            appendFormattedText(builder, "Salad: Với sốt nhẹ và một ít hạt như hạt chia hoặc hạt lanh.\n", false, false);
            appendFormattedText(builder, "Khoai lang: Nướng hoặc hấp để giảm lượng calo so với khoai tây chiên.\n\n", false, false);
            appendFormattedText(builder, "Các Bữa Ăn Nhẹ\n", true, true);
            appendFormattedText(builder, "Trái cây tươi: Như táo, lê, hoặc quả mọng.\n", false, false);
            appendFormattedText(builder, "Hạt: Như hạnh nhân hoặc hạt điều, không thêm đường.\n", false, false);
            appendFormattedText(builder, "Sữa chua ít béo hoặc phô mai ít béo: Có thể kèm theo một ít trái cây hoặc mật ong.\n\n", false, false);
            appendFormattedText(builder, "Thức Uống\n", true, true);
            appendFormattedText(builder, "Nước: Uống nhiều nước và tránh các loại nước ngọt có đường.\n", false, false);
            appendFormattedText(builder, "Trà xanh hoặc trà thảo mộc: Có thể giúp hỗ trợ quá trình giảm cân.\n", false, false);
            appendFormattedText(builder, "Nước ép trái cây tươi: Uống không thêm đường và ở mức độ vừa phải.\n\n", false, false);
            appendFormattedText(builder, "Một Số Lưu Ý\n", true, true);
            appendFormattedText(builder, "Kiểm soát lượng calo: Giảm lượng calo tiêu thụ và tập trung vào thực phẩm ít calo nhưng giàu dinh dưỡng.\n", false, false);
            appendFormattedText(builder, "Ăn nhiều bữa nhỏ: Thay vì ba bữa lớn, hãy ăn nhiều bữa nhỏ hơn trong ngày để duy trì cảm giác no.\n", false, false);
            appendFormattedText(builder, "Tập thể dục đều đặn: Kết hợp các bài tập cardio và tăng cường cơ bắp.\n", false, false);

        } else if (bmi >= 30) {
            appendFormattedText(builder, "Chỉ số BMI của bạn lớn hơn 30, bạn thuộc nhóm béo phì. Việc giảm cân là quan trọng để cải thiện sức khỏe. Dưới đây là gợi ý thực đơn hỗ trợ giảm cân:\n\n", true, false);
            appendFormattedText(builder, "Bữa Sáng\n", true, true);
            appendFormattedText(builder, "Yến mạch: Kèm theo một ít quả mọng và hạt chia hoặc hạt lanh.\n", false, false);
            appendFormattedText(builder, "Sữa chua ít béo: Với trái cây tươi và một ít hạt lanh.\n", false, false);
            appendFormattedText(builder, "Trứng luộc: Kèm theo rau xanh và một lát bánh mì nguyên cám.\n\n", false, false);
            appendFormattedText(builder, "Bữa Trưa\n", true, true);
            appendFormattedText(builder, "Salad lớn: Rau xanh, cà chua, dưa chuột, và một nguồn protein như ức gà hoặc cá nướng.\n", false, false);
            appendFormattedText(builder, "Cơm gạo lứt: Kèm theo một phần nhỏ và nhiều rau củ.\n", false, false);
            appendFormattedText(builder, "Soup rau củ: Giúp cung cấp chất xơ và vitamin mà không thêm nhiều calo.\n\n", false, false);
            appendFormattedText(builder, "Bữa Tối\n", true, true);
            appendFormattedText(builder, "Thịt nạc hoặc cá: Kèm theo rau hấp hoặc nướng và một phần nhỏ tinh bột như khoai lang.\n", false, false);
            appendFormattedText(builder, "Salad: Với sốt nhẹ và không thêm dầu hoặc đường.\n", false, false);
            appendFormattedText(builder, "Khoai lang: Nướng hoặc hấp để giảm lượng calo.\n\n", false, false);
            appendFormattedText(builder, "Các Bữa Ăn Nhẹ\n", true, true);
            appendFormattedText(builder, "Trái cây tươi: Như táo, lê, hoặc quả mọng, tránh trái cây có nhiều đường.\n", false, false);
            appendFormattedText(builder, "Hạt: Như hạnh nhân hoặc hạt chia, không thêm đường.\n", false, false);
            appendFormattedText(builder, "Sữa chua ít béo: Có thể kèm theo một ít trái cây tươi hoặc hạt chia.\n\n", false, false);
            appendFormattedText(builder, "Thức Uống\n", true, true);
            appendFormattedText(builder, "Nước: Uống nhiều nước và tránh các loại nước ngọt có đường.\n", false, false);
            appendFormattedText(builder, "Trà xanh hoặc trà thảo mộc: Có thể hỗ trợ quá trình giảm cân.\n", false, false);
            appendFormattedText(builder, "Nước ép trái cây tươi: Uống không thêm đường và ở mức độ vừa phải.\n\n", false, false);
            appendFormattedText(builder, "Một Số Lưu Ý\n", true, true);
            appendFormattedText(builder, "Kiểm soát lượng calo: Giảm lượng calo tiêu thụ và tập trung vào thực phẩm ít calo nhưng giàu dinh dưỡng.\n", false, false);
            appendFormattedText(builder, "Ăn nhiều bữa nhỏ: Thay vì ba bữa lớn, hãy ăn nhiều bữa nhỏ hơn trong ngày để duy trì cảm giác no.\n", false, false);
            appendFormattedText(builder, "Tập thể dục đều đặn: Kết hợp các bài tập cardio và tăng cường cơ bắp để hỗ trợ giảm cân.\n", false, false);
        }

        return builder;
    }

    private void appendFormattedText(SpannableStringBuilder builder, String text, boolean isTitle, boolean increaseSize) {
        SpannableString spannableString = new SpannableString(text);

        if (isTitle) {
            spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (increaseSize) {
            spannableString.setSpan(new AbsoluteSizeSpan(20, true), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            spannableString.setSpan(new AbsoluteSizeSpan(18, true), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        builder.append(spannableString);
    }
}