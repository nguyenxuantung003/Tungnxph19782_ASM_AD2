package com.example.nguyenxuantung_ph19782.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nguyenxuantung_ph19782.adapter.ExerciseAdapter;
import com.example.nguyenxuantung_ph19782.R;

public class ExerciseListActivity extends AppCompatActivity {

    private String[] exercises = {"Thiền", "Yoga", "Hít thở sâu"};
    private String[] exerciseDetails = {
            "Thiền: Thiền là một phương pháp thực hành giúp tâm trí thư giãn, tăng cường sự tập trung và tạo ra sự bình an nội tâm. Thiền có nhiều hình thức khác nhau, nhưng mục tiêu chính là để tạo ra sự tĩnh lặng trong tâm trí và làm chủ cảm xúc",
            "Yoga: Yoga là một hệ thống luyện tập cổ xưa có nguồn gốc từ Ấn Độ, bao gồm các phương pháp rèn luyện thể chất, tinh thần và tâm linh. Mục tiêu chính của yoga là đạt được sự cân bằng và hòa hợp giữa cơ thể, tâm trí và tinh thần",
            "Hít thở sâu: Phương pháp hít thở sâu, còn gọi là thở bụng hoặc thở diafragmatic, giúp cải thiện lượng oxy cung cấp cho cơ thể, giảm căng thẳng, và tăng cường sự thư giãn. Đây là một kỹ thuật hít thở đơn giản nhưng rất hiệu quả trong việc làm dịu tâm trí và cải thiện sức khỏe tổng thể."
    };
    private String[] exerciseBenefits = {
            "Lợi ích: Giảm căng thẳng và lo âu: Thiền có thể giúp giảm mức độ căng thẳng và lo âu.\n" +
                    "Cải thiện sự tập trung: Tăng cường khả năng tập trung và chú ý.\n" +
                    "Tăng cường sức khỏe tâm lý: Cải thiện cảm giác hạnh phúc và sự tự tin.\n\n",
            "Lợi ích: Cải thiện sức khỏe thể chất\n" +
                    "\n" +
                    "Tăng cường sức mạnh và linh hoạt: Giúp cơ thể trở nên dẻo dai hơn và giảm nguy cơ chấn thương.\n" +
                    "Cải thiện sự cân bằng và phối hợp: Tăng cường khả năng điều khiển cơ thể và tư thế.\n" +
                    "Giảm căng thẳng và lo âu\n" +
                    "\n" +
                    "Thư giãn tinh thần: Thiền và hơi thở giúp giảm căng thẳng và lo âu, cải thiện cảm giác bình yên.\n" +
                    "Tăng cường sức khỏe tâm lý\n" +
                    "\n" +
                    "Tăng cường sự tập trung: Cải thiện khả năng tập trung và nhận thức.\n" +
                    "Cải thiện cảm giác hạnh phúc: Giúp nâng cao tâm trạng và tự tin.\n" +
                    "Cải thiện chất lượng giấc ngủ\n" +
                    "\n" +
                    "Giúp ngủ ngon hơn: Thực hành yoga có thể giúp cải thiện chất lượng giấc ngủ và giảm triệu chứng mất ngủ.\n\n",
            "Lợi ích: Giảm căng thẳng và lo âu: Giúp làm dịu hệ thần kinh và giảm mức độ căng thẳng.\n" +
                    "Cải thiện sự tập trung: Tăng cường sự tập trung và làm rõ tâm trí.\n" +
                    "Tăng cường cung cấp oxy: Cải thiện lưu thông oxy và tăng cường chức năng của các cơ quan.\n" +
                    "Cải thiện sự thư giãn: Giúp cơ thể và tâm trí cảm thấy thư giãn hơn.\n\n"
    };
    private String[] exerciseInstructions = {
            "Cách thực hiện: Chọn Thời Gian và Nơi Thực Hiện\n" +
                    "\n" +
                    "Thời gian: Chọn một thời điểm trong ngày mà bạn có thể thiền mà không bị làm phiền, chẳng hạn như buổi sáng hoặc buổi tối.\n" +
                    "Nơi yên tĩnh: Tìm một nơi yên tĩnh và thoải mái để thực hành.\n" +
                    "Ngồi Đúng Tư Thế\n" +
                    "\n" +
                    "Tư thế: Ngồi ở một vị trí thoải mái với lưng thẳng. Bạn có thể ngồi trên ghế hoặc trên sàn với gối đệm.\n" +
                    "Tập Trung Vào Hơi Thở\n" +
                    "\n" +
                    "Hơi thở: Đưa sự chú ý vào hơi thở của bạn, cảm nhận hơi thở ra vào một cách tự nhiên.\n" +
                    "Chấp Nhận Suy Nghĩ\n" +
                    "\n" +
                    "Suy nghĩ: Khi bạn nhận thấy suy nghĩ hoặc cảm xúc xuất hiện, chỉ đơn giản là quan sát chúng mà không bị cuốn vào.\n" +
                    "Kết Thúc Thực Hành\n" +
                    "\n" +
                    "Từ từ mở mắt: Sau khi hoàn thành, từ từ mở mắt và dành một chút thời gian để cảm nhận sự bình yên và sự tĩnh lặng trước khi tiếp tục hoạt động.\n\n",
            "Cách thực hiện: Chọn loại yoga phù hợp\n" +
                    "\n" +
                    "Hatha Yoga: Tập trung vào các tư thế cơ bản và hơi thở.\n" +
                    "Vinyasa Yoga: Liên kết các tư thế với hơi thở và chuyển động liên tục.\n" +
                    "Yin Yoga: Tập trung vào sự thư giãn và kéo dài cơ thể.\n" +
                    "Power Yoga: Phiên bản yoga mạnh mẽ hơn với các bài tập thể lực cao.\n" +
                    "Tạo không gian tập luyện\n" +
                    "\n" +
                    "Chọn nơi yên tĩnh: Một không gian thoải mái và yên tĩnh để thực hành.\n" +
                    "Sử dụng thảm yoga: Để tạo sự thoải mái và hỗ trợ trong các tư thế.\n" +
                    "Lắng nghe cơ thể\n" +
                    "\n" +
                    "Điều chỉnh tư thế: Lắng nghe cơ thể và điều chỉnh các tư thế để phù hợp với khả năng của bạn.\n" +
                    "Thực hành đều đặn: Thực hành yoga đều đặn để đạt được lợi ích tốt nhất.\n\n",
            "Cách thực hiện: Tìm Nơi Yên Tĩnh\n" +
                    "\n" +
                    "Chọn nơi thoải mái: Ngồi hoặc nằm ở một nơi yên tĩnh, nơi bạn có thể thư giãn mà không bị làm phiền.\n" +
                    "Ngồi hoặc Nằm Đúng Tư Thế\n" +
                    "\n" +
                    "Tư thế ngồi: Ngồi thẳng lưng trên ghế với chân đặt phẳng trên sàn.\n" +
                    "Tư thế nằm: Nằm ngửa trên mặt phẳng với tay để thoải mái ở hai bên cơ thể.\n" +
                    "Đặt Tay Đúng Vị Trí\n" +
                    "\n" +
                    "Đặt tay lên bụng: Đặt một tay lên bụng và tay còn lại lên ngực để cảm nhận chuyển động của cơ thể khi bạn thở.\n" +
                    "Hít Thở Sâu\n" +
                    "\n" +
                    "Hít vào từ từ qua mũi: Hít vào bằng mũi một cách chậm rãi, để bụng phình ra và tay trên bụng cảm nhận được sự mở rộng. Cố gắng giữ cho ngực ổn định và ít chuyển động nhất có thể.\n" +
                    "Giữ hơi thở: Có thể giữ hơi thở trong vài giây nếu cảm thấy thoải mái.\n" +
                    "Thở Ra Chậm Rãi\n" +
                    "\n" +
                    "Thở ra qua miệng: Thở ra từ từ qua miệng, để bụng co lại. Cảm nhận hơi thở ra hết hoàn toàn và làm cho bụng thắt lại.\n" +
                    "Lặp Lại\n" +
                    "\n" +
                    "Lặp lại quá trình: Thực hiện các bước hít vào và thở ra từ 5 đến 10 lần. Mỗi lần hít thở nên kéo dài khoảng 3-4 giây và thở ra cũng tương tự.\n\n"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        ListView listView = findViewById(R.id.exercise_list_view);

        ExerciseAdapter adapter = new ExerciseAdapter(this, exercises);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(ExerciseListActivity.this, ExerciseDetailActivity.class);
            intent.putExtra("exercise_name", exercises[position]);
            intent.putExtra("exercise_description", exerciseDetails[position]);
            intent.putExtra("exercise_benefits", exerciseBenefits[position]);
            intent.putExtra("exercise_instructions", exerciseInstructions[position]);
            startActivity(intent);
        });
    }
}