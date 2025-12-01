
package core.datos.dto;


public class DonacionDTO {
    private int idDonacion;
    private int idCampania;
    private int idUsuarioDonante;
    private double monto;
    private String entidadBancaria;
    private String medioPago;
    private String destinoRecursos;
    private String referenciaPago;
    private String fecha;

    public int getIdDonacion() {
        return idDonacion;
    }

    public void setIdDonacion(int idDonacion) {
        this.idDonacion = idDonacion;
    }

    public int getIdCampana() {
        return idCampania;
    }

    public void setIdCampana(int idCampana) {
        this.idCampania = idCampana;
    }

    public int getIdUsuarioDonante() {
        return idUsuarioDonante;
    }

    public void setIdUsuarioDonante(int idUsuarioDonante) {
        this.idUsuarioDonante = idUsuarioDonante;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEntidadBancaria() {
        return entidadBancaria;
    }

    public void setEntidadBancaria(String entidadBancaria) {
        this.entidadBancaria = entidadBancaria;
    }

    public String getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(String medioPago) {
        this.medioPago = medioPago;
    }

    public String getDestinoRecursos() {
        return destinoRecursos;
    }

    public void setDestinoRecursos(String destinoRecursos) {
        this.destinoRecursos = destinoRecursos;
    }

    public String getReferenciaPago() {
        return referenciaPago;
    }

    public void setReferenciaPago(String referenciaPago) {
        this.referenciaPago = referenciaPago;
    }
}
