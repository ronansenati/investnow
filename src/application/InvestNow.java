package application;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import entities.time;

public class InvestNow {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		Locale.setDefault(Locale.US);

		String dateInic;
		String dateFinal;

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date dataInicio;
		Date dataFim;

		LocalDate dateBegin = null;
		LocalDate dateEnd = null;

		Double montante;
		String nome;
		Double c;
		Double i;
		Double imposto;
		Double aliq;

		System.out.println("Nome do banco do título");
		// nome = sc.nextLine();
		nome = "Banco Original";
		System.out.println("Capital inicial:");
		// c = sc.nextDouble();
		c = 80000.0;
		System.out.println("Taxa de Juros:");
		// i = sc.nextDouble();
		i = 10.5;
		System.out.println("Aliquota de IR:");
		// aliq = sc.nextDouble();
		aliq = 15.0;
		System.out.println("Data inicial  do investimento:");
		// dateInic = sc.next();
		dateInic = "06/12/2017";
		System.out.println("Data fim  do investimento:");
		// dateFinal = sc.next();
		dateFinal = "20/11/2020";

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
		dateBegin = LocalDate.parse(dateInic, formatter);
		dateEnd = LocalDate.parse(dateFinal, formatter);

		dataInicio = format.parse(dateInic);
		dataFim = format.parse(dateFinal);	

		final double MES_EM_MILISEGUNDOS = 30.0 * 24.0 * 60.0 * 60.0 * 1000.0;
		// final double MES_EM_MILISEGUNDOS = 2592000000.0;

		int numeroDeMeses = (int) ((dataFim.getTime() - dataInicio.getTime()) / MES_EM_MILISEGUNDOS);
		System.out.println("Número de anos: " + numeroDeMeses/12);
		System.out.println("Número de meses: " + numeroDeMeses);
		long dt = (dataFim.getTime() - dataInicio.getTime()) + 3600000; // 1 hora para compensar horário de verão
		int dias = (int) (dt / 86400000L);
		System.out.println("Número de dias: " + dias); // passaram-se 67111 dias
		long days = entities.time.contaDiasUteisEntre(dateBegin, dateEnd);
		System.out.println("Dias úteis: " + days);
		int feriados = time.numeroFeriados(dateInic, dateFinal, "C:\\Users\\ronan\\Downloads\\feriados_nacionais.csv");
		System.out.println("Número de feriados: " + feriados);
		System.out.println("Dias úteis desconsiderando feriados: " + (days - feriados));
		double diasUteis = (days - feriados);

		DecimalFormat df = new DecimalFormat("###,###0.000");

		//System.out.println(df.format((diasUteis / (diasUteis + 2))));
		double expo = Double.parseDouble(df.format((diasUteis / (diasUteis + 2))));

		montante = c * (Math.pow((1 + (i / 100)), expo*(numeroDeMeses/12)));
		imposto = (montante - c) * (aliq / 100);

		System.out.println(" ");
		System.out.println("Nome: " + nome);
		System.out.println("Capital inicial: " + String.format("%.2f", c));
		System.out.println("Valor bruto: " + String.format("%.2f", montante));
		System.out.println("Imposto de Renda Aliq. " + aliq + "%: " + String.format("%.2f", imposto));
		System.out.println("Valor bruto: " + String.format("%.2f", montante));
		System.out.println("Valor Líquido CDB: " + String.format("%.2f", montante - imposto));
		System.out.println("Lucro líquido CDB: " + String.format("%.2f", montante - imposto - c));
		// System.out.println("Montante líquido LC: "+ (montante));

		/*
		 * M = C x (1 + i)^n
		 * 
		 * M = Montante C= Capital Inicial i = Taxa de Juros. ex. 10% = 0,1 n= Tempo.
		 * 
		 */
		sc.close();

	}

}
