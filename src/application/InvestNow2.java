package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import db.DbException;
import entities.MigraEntities;
import entities.time;

public class InvestNow2 {

	public static String path = null;

	public static void main(String[] args) {

		System.out.println(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));

		try {
			Scanner sc = new Scanner(System.in);
			Locale.setDefault(Locale.US);

			String dateInic;
			String dateFinal;
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			Date dataInicio;
			Date dataFim;

			LocalDate dateBegin = null;
			LocalDate dateEnd = null;
			LocalDate dateEndNow = null;

			double montante;
			double imposto;
			double montanteNow;
			double impostoNow;
			
			String nome;
			double c;
			double i;
			double aliq;
			double aliqHoje;
			double sumLucro = 0.0;
			double sumLucro2 = 0.0;
			double capitalInicialTotal = 0.0;
			double valorTotalBrutoNoVencimento = 0.0;
			double valorTotalBrutoAtual = 0.0;
			double ValorTotalLiquidoNoVencimento = 0.0;
			double valorTotalImpostoNoVencimento = 0.0;	
			String dataFormatada = null;
		
			path = "C:\\Users\\ronan\\eclipse-workspace\\InvestNow";

			// String path = sc.nextLine();

			MigraEntities mg = new MigraEntities();
			List<String> listOrigem = new ArrayList<>();
			listOrigem = mg.listFoldersBetter(path);

			for (String list : listOrigem) {

				File[] arrayFile = null;
				File file = new File(list);
				arrayFile = file.listFiles();

				for (File arquivo : arrayFile) {

					if (arquivo.getName().startsWith("Exportar_custodia_") & arquivo.getName().endsWith(".csv")) {
						BufferedReader br = new BufferedReader(new FileReader(arquivo));
						String line = br.readLine();

						while (line != null) {

							String[] fields = line.split(";");

							String campo1 = fields[0];
							String tipoTitulo = campo1;
							String campo2 = fields[1];
							nome = campo2;
							String campo3 = fields[2];
							dateInic = campo3;
							String campo4 = fields[3];
							dateFinal = campo4;
							String campo5 = fields[4];
							i = Double.parseDouble(campo5);
							String campo6 = fields[5];
							c = Double.parseDouble(campo6.replace(",", "."));
							String campo7 = fields[6];
							Double valorBruto = Double.parseDouble(campo7.replace(",", "."));
							String campo8 = fields[7];
							campo8.replace(",", ".");

							Date dNow = new Date();
							SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
							dataFormatada = formatador.format(dNow);
							final double MES_EM_MILISEGUNDOS = 30.0 * 24.0 * 60.0 * 60.0 * 1000.0;							
							DecimalFormat df = new DecimalFormat("###,###0.000");
							DecimalFormat ano = new DecimalFormat("###,#0.0");
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
							dateBegin = LocalDate.parse(dateInic, formatter);
							dateEnd = LocalDate.parse(dateFinal, formatter);

							dataInicio = format.parse(dateInic);
							dataFim = format.parse(dateFinal);	
							
							// final double MES_EM_MILISEGUNDOS = 2592000000.0;
							
							/*
							
							System.out.println(" ");
							System.out.println("==== DADOS ATÉ A DATA DE HOJE: " + dataFormatada + "==== ");
							System.out.println(" ");
							System.out.println("Nome: " + campo1 + " - " + nome);
							System.out.println("Capital inicial: " + String.format("%.2f", c));
							System.out.println("Data inicial: " + dateInic);
							System.out.println("Taxa de juros: " + String.format("%.2f", i));

										

							double numeroDeMesesHoje = (int) ((dNow.getTime() - dataInicio.getTime())
									/ MES_EM_MILISEGUNDOS);

							dateEndNow = LocalDate.parse(dataFormatada, formatter);
							long daysNow = entities.time.contaDiasUteisEntre(dateBegin, dateEndNow);
							int feriadosNow = time.numeroFeriados(dateInic, dataFormatada,
									"C:\\Users\\ronan\\Downloads\\feriados_nacionais.csv");

							long dtNow = (dNow.getTime() - dataInicio.getTime()) + 3600000; // 1 hora para compensar
							// horário de verão
							int diasNow = (int) (dtNow / 86400000L);
						
										
							
							double diasUteisNow = (daysNow - feriadosNow);
							aliqHoje = entities.MigraEntities.taxaIR(diasNow);
							double expoNow = Double.parseDouble(df2.format((diasUteisNow / (diasUteisNow + 2))));
							double resultTaxaIR = 1 + (i / 100) ;
							double resultExpoNow = expoNow * (numeroDeMesesHoje / 12);
							resultExpoNow = Double.valueOf(df2.format(resultExpoNow));
							double result = Math.pow(resultTaxaIR, resultExpoNow);
							montanteNow = c * result;
							impostoNow = (montanteNow - c) * (aliqHoje / 100);
							
					//		1,46 resultExpoNow
					//		1,1574
					
							System.out.println("Número de anos até hoje: " + (int)numeroDeMesesHoje / 12);
							System.out.println("Número de meses até hoje: " + (int)numeroDeMesesHoje);
							System.out.println("Número de dias até hoje: " + diasNow);
							System.out.println("Dias úteis até hoje: " + daysNow);
							System.out.println("Número de feriados até hoje: " + feriadosNow);
							System.out.println(
									"Dias úteis desconsiderando feriados até hoje: " + (daysNow - feriadosNow));							
							if(tipoTitulo.equals("LCI") | tipoTitulo.equals("LCA")| tipoTitulo.equals("POUP")) {
								impostoNow=0.0;
								aliqHoje = 0.0;	
								System.out
								.println("Imposto de Renda Aliq. " + aliqHoje + "%: ISENTO");
							}else {
								System.out.println("Imposto de Renda Aliq. até hoje " + aliqHoje + "%: "
										+ String.format("%.2f", impostoNow));
							}							
							System.out.println("Valor bruto até hoje: " + String.format("%.2f", montanteNow));
							System.out.println("Valor Líquido até hoje: " + campo1 + ": "
									+ String.format("%.2f", montanteNow - impostoNow));
							System.out.println("Lucro líquido até hoje: " + campo1 + ": "
									+ String.format("%.2f", montanteNow - impostoNow - c));
							
						
							*/
						

							System.out.println(" ");
							System.out.println("==== DADOS NO VENCIMENTO ==== ");
							System.out.println(" ");

							double numeroDeMeses = (int) ((dataFim.getTime() - dataInicio.getTime())
									/ MES_EM_MILISEGUNDOS);
							long dt = (dataFim.getTime() - dataInicio.getTime()) + 3600000; // 1 hora para compensar
							// horário de verão
							int dias = (int) (dt / 86400000L);
							long days = entities.time.contaDiasUteisEntre(dateBegin, dateEnd);
							int feriados = time.numeroFeriados(dateInic, dateFinal,
									path + "\\feriados_nacionais.csv");
							double diasUteis = (days - feriados);
							aliq = entities.MigraEntities.taxaIR(dias);
							double expo = Double.parseDouble(df.format((diasUteis / (diasUteis + 2))));
							montante = c * (Math.pow((1 + (i / 100)), expo * (numeroDeMeses / 12)));							
							imposto = (montante - c) * (aliq / 100);

						
							System.out.println("Nome: " + campo1 + " - " + nome);
							System.out.println("Capital inicial: " + String.format("%.2f", c));
							System.out.println("Data inicial: " + dateInic);
							System.out.println("Data final: " + dateFinal);
							System.out.println("Taxa de juros: " + String.format("%.2f", i));

							System.out.println("Número de anos: " + Double.parseDouble(ano.format((numeroDeMeses / 12))));
							System.out.println("Número de meses: " + (int)numeroDeMeses);
							System.out.println("Número de dias: " + dias);
							System.out.println("Dias úteis: " + days);
							System.out.println("Número de feriados: " + feriados);
							System.out.println("Dias úteis desconsiderando feriados: " + (days - feriados));
							
							if(tipoTitulo.equals("LCI") | tipoTitulo.equals("LCA")| tipoTitulo.equals("POUP")) {
								imposto=0.0;
								aliq = 0.0;	
								System.out
								.println("Imposto de Renda Aliq. " + aliq + "%: ISENTO");
							}else {
								System.out
								.println("Imposto de Renda Aliq. " + aliq + "%: " + String.format("%.2f", imposto));
							
							}
					
							System.out.println("Valor bruto: " + String.format("%.2f", montante));
							System.out.println(
									"Valor Líquido " + campo1 + ": " + String.format("%.2f", montante - imposto));
							System.out.println(
									"Lucro líquido " + campo1 + ": " + String.format("%.2f", montante - imposto - c));

							double lucro = (montante - imposto - c);
							double lucro2 = lucro / numeroDeMeses;
							sumLucro += lucro;
							sumLucro2 += lucro2;
							capitalInicialTotal +=c;
							valorTotalBrutoNoVencimento+=montante;
							valorTotalImpostoNoVencimento+=imposto;
							ValorTotalLiquidoNoVencimento+=(montante - imposto);							
							valorTotalBrutoAtual+=valorBruto;							
							System.out.println("Lucro líquido mensal: " + String.format("%.2f", lucro2));

							// System.out.println("Montante líquido LC: "+ (montante));

							/*
							 * M = C x (1 + i)^n
							 * 
							 * M = Montante C= Capital Inicial i = Taxa de Juros. ex. 10% = 0,1 n= Tempo.
							 * 
							 */
							sc.close();

							line = br.readLine();
													
						
						}
						String nomeFile = arquivo.getName() ;
						String dataFile = nomeFile.substring(18, 29);
						br.close();
						System.out.println(" ");
						
						System.out.println("Capital inicial total: " + String.format("%.2f", capitalInicialTotal));
						System.out.println("Lucro líquido total no vencimento: " + String.format("%.2f", sumLucro));
						System.out.println("Valor líquido total + Capital inicial total no vencimento: " + String.format("%.2f", ValorTotalLiquidoNoVencimento));
						System.out.println("Valor bruto total no vencimento: " + String.format("%.2f", valorTotalBrutoNoVencimento));
						System.out.println("Valor bruto total na data do última carga de custódia "+ dataFile.format(dataFormatada,dataFile) +" : " + String.format("%.2f", valorTotalBrutoAtual));
						System.out.println("Valor total do imposto IR no vencimento: " + String.format("%.2f", valorTotalImpostoNoVencimento));
						System.out.println("Renda mensal estimada no vencimento: " + String.format("%.2f", sumLucro2));
						System.out.println(" ");
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new DbException(e.getMessage());
		} finally {
			System.out.println("THE END!");
		}
	}

}
