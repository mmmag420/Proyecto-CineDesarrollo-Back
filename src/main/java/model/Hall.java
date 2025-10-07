package model;

public class Hall {
	
	private int numSala;
	private Movie movie;
	private String diaPelicula;
	private String horaInicio;
	private String horaFin;
	private Chair[] sillas;
	
	public Hall(int numSala, Movie movie, String diaPelicula, String horaInicio, String horaFin, Chair[] sillas) {
		this.numSala = numSala;
		this.movie = movie;
		this.diaPelicula = diaPelicula;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.sillas = sillas;
	}

	public int getNumSala() {
		return numSala;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public String getDiaPelicula() {
		return diaPelicula;
	}

	public void setDiaPelicula(String diaPelicula) {
		this.diaPelicula = diaPelicula;
	}

	public String getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	public String getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(String horsFin) {
		this.horaFin = horsFin;
	}

	public Chair[] getSillas() {
		return sillas;
	}

	public void setSillas(Chair[] sillas) {
		this.sillas = sillas;
	}

}
