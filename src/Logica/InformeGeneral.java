package Logica;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Resultado de solo lectura; no representa una tabla ni se guarda en MariaDB. */
public final class InformeGeneral {
    public static final String ESTUDIANTES = "Estudiantes";
    public static final String DOCENTES = "Docentes";
    public static final String ASIGNATURAS = "Asignaturas";
    public static final String INSCRIPCIONES = "Inscripciones";
    public static final String CALIFICACIONES = "Calificaciones";
    public static final String INFORMES_ACADEMICOS = "Informes académicos";
    public static final List<String> TIPOS = List.of(ESTUDIANTES, DOCENTES,
            ASIGNATURAS, INSCRIPCIONES, CALIFICACIONES, INFORMES_ACADEMICOS);

    private final String tipo;
    private final List<String> columnas;
    private final List<List<String>> filas;
    private final LocalDateTime generadoEn;

    public InformeGeneral(String tipo, List<String> columnas, List<List<String>> filas) {
        this.tipo = Objects.requireNonNull(tipo);
        this.columnas = List.copyOf(Objects.requireNonNull(columnas));
        List<List<String>> copia = new ArrayList<>();
        for (List<String> fila : Objects.requireNonNull(filas)) {
            if (fila.size() != columnas.size()) {
                throw new IllegalArgumentException("Cantidad de columnas inconsistente");
            }
            copia.add(List.copyOf(fila));
        }
        this.filas = List.copyOf(copia);
        this.generadoEn = LocalDateTime.now();
    }

    public String getTipo() { return tipo; }
    public List<String> getColumnas() { return columnas; }
    public List<List<String>> getFilas() { return filas; }
    public LocalDateTime getGeneradoEn() { return generadoEn; }
}
