package entities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import db.DbException;

public class time {

	private static final DayOfWeek SATURDAY = null;
	private static final DayOfWeek SUNDAY = null;

	public int workingDay(Date dataInic, Date dataFim) {

		int ano = 2014;
		int mes = 5;
		YearMonth anoMes = YearMonth.of(ano, mes);

		List<LocalDate> listaDosDiasUteisDoMes = Stream.iterate(anoMes.atDay(1), data -> data.plusDays(1))
				.limit(anoMes.lengthOfMonth()).filter(data -> !data.getDayOfWeek().equals(DayOfWeek.SATURDAY)
						&& !data.getDayOfWeek().equals(DayOfWeek.SUNDAY))
				.collect(Collectors.toList());
		int day = 0;
		for (LocalDate list : listaDosDiasUteisDoMes) {
			day++;
			System.out.println(list.getDayOfMonth() + " - " + day);
		}
		return day;
	}

	public static boolean isFinalDeSemana(LocalDate date) {
		return date.getDayOfWeek() == SATURDAY || date.getDayOfWeek() == SUNDAY;
	}

	public static long contaDiasUteisEntre(LocalDate start, LocalDate end) {
		long dias;

		long finsDeSemana;

		dias = ChronoUnit.DAYS.between(start, end);

		long semanas = ChronoUnit.WEEKS.between(start, end);
		finsDeSemana = semanas * 2;

		dias = dias - finsDeSemana;

		// Elimina o caso de comparar duas datas que caem no fim de semana com o mesmo
		// dia
		if ((start.getDayOfWeek() == SATURDAY && end.getDayOfWeek() == SATURDAY)
				|| (start.getDayOfWeek() == SUNDAY && end.getDayOfWeek() == SUNDAY)) {
			dias--;
		}

		while (start.getDayOfWeek().compareTo(end.getDayOfWeek()) > 0) {

			if (isFinalDeSemana(start)) {
				dias--;
			}

			start = start.plusDays(1);
		}

		return dias;
	}

	public static int numeroFeriados(String dateInic, String dateFinal, String arquivoFeriado) {
		try {

			// TODO Auto-generated method stub
			Scanner sc = new Scanner(System.in);
			Locale.setDefault(Locale.US);				
			int count = -1;
		

			BufferedReader br = new BufferedReader(
					new FileReader(arquivoFeriado));
			String line = br.readLine();

			while (line != null) {

				String[] fields = line.split(";");

				String dataFeriado = fields[0];

				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

				Date d1 = sdf.parse(dateInic);
				Date d2 = sdf.parse(dateFinal);
				Date d3 = sdf.parse(dataFeriado);

				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				Calendar cal3 = Calendar.getInstance();

				cal.setTime(d1);
				cal2.setTime(d2);
				cal3.setTime(d3);

				if (cal3.after(cal)) {

					count++;
					if (cal2.before(cal3)) {
						
						break;
					}
				}
				line = br.readLine();
			}
			br.close();
			sc.close();
			return count;

		} catch (Exception e) {
			e.printStackTrace();
			throw new DbException(e.getMessage());
		} finally {

		}
		
	}

}
