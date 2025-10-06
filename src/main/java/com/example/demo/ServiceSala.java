package com.example.demo;

import java.time.DayOfWeek;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import model.Chair;
import model.Hall;
import model.Movie;

@Service
public class ServiceSala {

	
	private final RepositorySala repo;
	private final ServiceMovie serviceMovie;
	private final int TOTAL_SILLAS = 39;
	
    private final Map<DayOfWeek, String[]> horariosSemana = Map.of(
            DayOfWeek.MONDAY,    new String[]{"16:50","19:10","21:30"},
            DayOfWeek.TUESDAY,   new String[]{"16:50","19:10","21:30"},
            DayOfWeek.WEDNESDAY, new String[]{"16:50","19:10","21:30"},
            DayOfWeek.THURSDAY,  new String[]{"16:50","19:10","21:30"},
            DayOfWeek.FRIDAY,    new String[]{"16:50","19:10","21:30"},
            DayOfWeek.SATURDAY,  new String[]{"14:30","16:50","19:10","21:30"},
            DayOfWeek.SUNDAY,    new String[]{"14:30","16:50","19:10","21:30"}
        );
	
	
	public ServiceSala(RepositorySala repo, ServiceMovie serviceMovie) {
		this.repo = repo;
		this.serviceMovie = serviceMovie;	
	}

    public void sembrarSemana(Movie peliSala1, Movie peliSala2, Movie peliSala3) {
        LocalDate base = LocalDate.now(); // hoy
        // Duraciones (puedes leerlas del Movie si tienes duración como minutos):
        int durSala1 = parseDuracionMinutos(peliSala1.getDuracion()); // "1h 55m" -> 115
        int durSala2 = parseDuracionMinutos(peliSala2.getDuracion());
        int durSala3 = parseDuracionMinutos(peliSala3.getDuracion());

        for (int d = 0; d < 7; d++) {
            LocalDate dia = base.plusDays(d);
            String diaISO = dia.toString();
            String[] horas = horariosSemana.getOrDefault(dia.getDayOfWeek(), new String[0]);

            crearFuncionesDelDia(1, peliSala1, diaISO, horas, durSala1);
            crearFuncionesDelDia(2, peliSala2, diaISO, horas, durSala2);
            crearFuncionesDelDia(3, peliSala3, diaISO, horas, durSala3);
        }
    }

    /*public void reprogramarSalaDesde(int sala, Movie nuevaPeli, LocalDate desde) {
        // 1) borrar funciones de esa sala desde 'desde'
        repo.borrarFuncionesSalaDesde(sala, desde);

        // 2) (re)sembrar desde 'desde' por 7 días (o el rango que quieras)
        int dur = parseDuracionMinutos(nuevaPeli.getDuracion());
        for (int d = 0; d < 7; d++) {
            LocalDate dia = desde.plusDays(d);
            String[] horas = horariosSemana.getOrDefault(dia.getDayOfWeek(), new String[0]);
            crearFuncionesDelDia(sala, nuevaPeli, dia.toString(), horas, dur);
        }
    }*/
      
    public List<Hall> listarFunciones(int sala, String diaISO) {
        return repo.listarPorSalaYDia(sala, diaISO);
    }
    
    public boolean reservarSilla(int sala, String diaISO, String horaInicio, int numSilla) {
        return repo.reservarSilla(sala, diaISO, horaInicio, numSilla);
    }
    
    public Hall detalleFuncion(int sala, String diaISO, String horaInicio) {
        return repo.buscarFuncion(sala, diaISO, horaInicio);
    }
	
    public boolean[] verSiEstaOcupada(int sala, String diaISO, String horaInicio) {
        Hall salaEx = repo.buscarFuncion(sala, diaISO, horaInicio);
        if (salaEx == null) return null;
        boolean[] ocup = new boolean[salaEx.getSillas().length];
        for (int i = 0; i < salaEx.getSillas().length; i++) ocup[i] = salaEx.getSillas()[i].isEstado();
        return ocup;
    }
	
		
    private void crearFuncionesDelDia(int sala, Movie movie, String diaISO, String[] horas, int durMin) {
        for (String hIni : horas) {
            LocalTime ini = LocalTime.parse(hIni);
            LocalTime fin = ini.plusMinutes(durMin);
            repo.crearFuncion(new Hall(
                    sala,
                    movie,
                    diaISO,
                    ini.toString(),
                    fin.toString(),
                    crearSillasLibres(TOTAL_SILLAS)
            ));
        }
    }
    
    private Chair[] crearSillasLibres(int total) {
        Chair[] arr = new Chair[total];
        for (int i = 0; i < total; i++) arr[i] = new Chair(i + 1, false);
        return arr;
    }
    
    private int parseDuracionMinutos(String dur) {
        try {
            int hIndex = dur.indexOf('h');
            int mIndex = dur.indexOf('m');
            int horas = hIndex > 0 ? Integer.parseInt(dur.substring(0, hIndex).trim()) : 0;
            int mins  = (mIndex > hIndex) ? Integer.parseInt(dur.substring(hIndex + 1, mIndex).trim()) : 0;
            return horas * 60 + mins;
        } catch (Exception e) {
            return 120; // por default 2h si no se puede parsear
        }
    }
	
	
}
