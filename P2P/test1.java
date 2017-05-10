package edu.ufl.alexgre.P2P;

public class test1 {
	public static void main(String[] args) {
		String s = "1.part";
		
		int a = 0;
		int b = s.indexOf(".");
		String m = s.substring(a, b);
		System.out.println(m);
	}
}
