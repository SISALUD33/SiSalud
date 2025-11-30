
package core.datos.dao.interfaces;


public interface IPacienteDAO {
    Integer obtenerIdPacientePorUsuario(int idUsuario);
    Integer obtenerOCrearPacientePorUsuario(int idUsuario);
}
