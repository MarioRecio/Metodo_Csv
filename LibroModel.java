/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aev4;

import com.csvreader.CsvReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DAM 2
 */
public class LibroModel extends DBUtil {

    public ArrayList<Libro> importarCSV(String ruta, String caracter) {
        ArrayList<Libro> listaLibros = new ArrayList<Libro>();

        try {
            CsvReader leerLibros = new CsvReader(ruta);
            leerLibros.readHeaders();
            leerLibros.setDelimiter(caracter.charAt(0));
            while (leerLibros.readRecord()) {

                String titulo = leerLibros.get(0);
                String autor = leerLibros.get(1);

                String nacimiento = "";

                if (leerLibros.get(2) != null) { //hay un libro que es null por eso se hace esto
                    nacimiento = leerLibros.get(2);
                } else {
                    nacimiento = "n.c";
                }
                String publicacion = leerLibros.get(3);
                String editorial = leerLibros.get(4);
                String paginas = leerLibros.get(5);

                listaLibros.add(new Libro(titulo, autor, nacimiento, publicacion, editorial, paginas));
            }
            leerLibros.close();

        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }

        return listaLibros;
    }

    public boolean insertarMySQL(List<Libro> listaLibros) {

        DBUtil db = new DBUtil();

        String query = "INSERT INTO libros(Titulo, Autor, Nacimiento, Publicacion, Editorial, Paginas) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            //db.datos(usuario, contraseña);
            PreparedStatement ps = db.getConexion().prepareStatement(query);

            for (int i = 0; i < listaLibros.size(); i++) {
                ps.setString(1, listaLibros.get(i).getTitulo());
                ps.setString(2, listaLibros.get(i).getAutor());
                ps.setString(3, listaLibros.get(i).getNacimiento());
                ps.setString(4, listaLibros.get(i).getPublicacion());
                ps.setString(5, listaLibros.get(i).getEditorial());
                ps.setString(6, listaLibros.get(i).getPaginas());

                ps.executeUpdate();

            }
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        } finally {
            db.cerrarConexion();
        }
    }

    public ArrayList<Libro> getLibros() {

        ArrayList<Libro> lista = new ArrayList<Libro>();

        try {
            String sql = "SELECT Titulo,Autor,Nacimiento,Publicacion,Editorial,Paginas FROM libros";
            PreparedStatement stmt = this.getConexion().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                String Titulo = rs.getString("Titulo");
                String Autor = rs.getString("Autor");
                String Nacimiento = rs.getString("Nacimiento");
                String Publicacion = rs.getString("Publicacion");
                String Editorial = rs.getString("Editorial");
                String Paginas = rs.getString("Paginas");

                Libro l = new Libro(Titulo, Autor, Nacimiento, Publicacion, Editorial, Paginas);

                lista.add(l);
            }

            return lista;

        } catch (SQLException e) {
            //e.printStackTrace();
            DBUtil.getDBUtil().error = e.toString();
            return null;
        } finally {
            //Cerramos conexión
            this.cerrarConexion();
        }
    }

}
