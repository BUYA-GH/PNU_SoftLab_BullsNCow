package mainpackage;

import java.util.Random;

public class Latlng {
	// 중앙, 새벽별, 넉터, 건설관, 항공,
	// 인문, 금정회관, 학생회관, 샛별회관, 문창회관,
	// 예술관, 생물관, 언어교육원, 법학관, 웅비의탑,
	// 실험동및전산원, 경영관, 산업협력관, 약학관, 자연과학관
	
	int number = 20;
	
	String [] name = {
			"mainLib", "secLib", "playground", "architecture", "aviation",
			"humanities", "geumjung", "student", "lucifer", "munchang",
			"art", "biology", "language", "law", "tower", 
			"studies", "business", "chemical", "industry", "nature"
	};
	
	String [] lat = {
			"35.233903", "35.235681", "35.231735", "35.231510", "35.233170",
			"35.231859", "35.235318", "35.235325", "35.234214", "35.233923",
			"35.232564", "35.234542", "35.235861", "35.236746", "35.230972",
			"35.234987", "35.236312", "35.235166", "35.232568", "35.2337672137374"
	};
	
	String [] longt = {
			"129.078703", "129.0816332", "129.082916", "129.080128", "129.08377",
			"129.081144", "129.080495", "129.076707", "129.079518", "129.081941",
			"129.077607", "129.081106", "129.083604", "129.078729", "129.0813167",
			"129.082492", "129.079920", "129.077992", "129.078467", "129.08083534111876"
	};
	int [] enable = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	
	public Latlng() {
		Random r = new Random();
		int a[] = new int[10];
		
		for(int i = 0; i < 10; ++i) {
			a[i] = r.nextInt(number);
			for(int j = 0; j < i; j++) {
				if(a[i] == a[j]) i--;
			}
		}
		
		for(int i = 0; i < 10; ++i) {
			enable[a[i]] = 1;
		}
	}
	
	public String getPacket(int i) {
		String pac = null;
		if(i>=0 && i < 20 && enable[i] == 1) {
			pac = "PIN:" + name[i] + ":" + lat[i] + ":" + longt[i];
		}

		return pac;
	}
	
	public void setPin(String pin, int n) {
		for(int i = 0; i < 20; ++i) {
			if(pin.equals(name[i])) enable[i] = 0;
		}
	}
}
