package common_txy;

import java.text.NumberFormat;


public class MainTest
{
	public static void main(String[] args)
	{
		NumberFormat format = NumberFormat.getInstance();
        format.setMinimumIntegerDigits(7);
        int i = 100101;
        String result = format.format(i).replace(",", "");
       System.out.println( result );
        
        int a = 5;
        String result_k = "xxxx" + String.format("%06d", a);
        System.out.println(result_k);
	}
}
