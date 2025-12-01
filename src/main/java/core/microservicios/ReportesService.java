package core.microservicios;

import core.datos.dto.UsuarioDTO;
import core.negocios.UsuarioNegocios;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class ReportesService {

    /**
     * Genera un Excel con todos los usuarios de la tabla USUARIO.
     *
     * @param destino archivo .xlsx donde se guardará el reporte
     * @return true si se generó correctamente
     */
    public static boolean generarReporteUsuariosExcel(File destino) {
        try {
            UsuarioNegocios usuarioNegocios = new UsuarioNegocios();
            List<UsuarioDTO> usuarios = usuarioNegocios.listarUsuarios();

            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet("Usuarios");

            // Encabezados
            String[] headers = {
                "ID Usuario",
                "Nombre",
                "Correo",
                "Teléfono",
                "ID Rol",
                "Activo",
                "Fecha registro"
            };

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Datos
            int rowIndex = 1;
            for (UsuarioDTO u : usuarios) {
                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(u.getIdUsuario());
                row.createCell(1).setCellValue(
                        u.getNombre() != null ? u.getNombre() : "");
                row.createCell(2).setCellValue(
                        u.getCorreo() != null ? u.getCorreo() : "");
                row.createCell(3).setCellValue(
                        u.getTelefono() != null ? u.getTelefono() : "");
                row.createCell(4).setCellValue(u.getIdRol());
                row.createCell(5).setCellValue(u.isActivo() ? "SI" : "NO");
                row.createCell(6).setCellValue(
                        u.getFechaRegistro() != null ? u.getFechaRegistro() : "");
            }

            // Auto-ajustar columnas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Guardar archivo
            try (FileOutputStream fos = new FileOutputStream(destino)) {
                wb.write(fos);
            }
            wb.close();

            System.out.println("Reporte de usuarios generado en: " + destino.getAbsolutePath());
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
