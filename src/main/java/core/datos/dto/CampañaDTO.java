package core.datos.dto;

public class CampañaDTO {

    private int idCampania;
    private String titulo;
    private String descripcion;
    private double meta;
    private double recaudado;
    private String estado;
    private Integer idPaciente;          
    private Integer idCuidador;
    private Integer idProfesionalSalud;
    private Integer idClinica;
    private String fechaCreacion;

    // Getters y Setters
    public int getIdCampania() {
        return idCampania;
    }

    public void setIdCampania(int idCampania) {
        this.idCampania = idCampania;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getMeta() {
        return meta;
    }

    public void setMeta(double meta) {
        this.meta = meta;
    }

    public double getRecaudado() {
        return recaudado;
    }

    public void setRecaudado(double recaudado) {
        this.recaudado = recaudado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    public Integer getIdCuidador() {
        return idCuidador;
    }

    public void setIdCuidador(Integer idCuidador) {
        this.idCuidador = idCuidador;
    }

    public Integer getIdProfesionalSalud() {
        return idProfesionalSalud;
    }

    public void setIdProfesionalSalud(Integer idProfesionalSalud) {
        this.idProfesionalSalud = idProfesionalSalud;
    }

    public Integer getIdClinica() {
        return idClinica;
    }

    public void setIdClinica(Integer idClinica) {
        this.idClinica = idClinica;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
     @Override
    public String toString() {
        if (titulo != null && !titulo.isBlank()) {
            return titulo + " (ID " + idCampania + ")";
        }
        return "Campaña #" + idCampania;
    }
}

