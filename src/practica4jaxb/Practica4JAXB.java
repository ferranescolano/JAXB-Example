package practica4jaxb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import model.Sudokus;
import model.Sudokus.Sudoku;
import modelHistory.Historial;
import modelHistory.Historial.Historico;
import modelUsuarios.Usuarios;
import modelUsuarios.Usuarios.Usuario;
//import model.Sudokus.Sudoku;

public class Practica4JAXB {

    private static FileReader leer = null;
    private static BufferedReader br = null;
    static String lector = null;
    private static String linea1;
    private static String linea2;

    private static String linea3;
    private static Usuarios arrayUsuarios = new Usuarios();
    private static Sudokus arraySudokus = new Sudokus();
    private static Historial arrayHistorial = new Historial();
    private static File fileSudokus = new File("sudoku.xml");
    private static File txtsudokus = new File("sudokus.txt");
    private static File fileUsuarios = new File("usuario.xml");
    private static File fileHistorial = new File("historial.xml");
    private static String nombreUsuarioActual;

    public static void main(String[] args) {

        int opcion;

        try {

            cargarDatos();
            cargarDatosUsuario();
            cargarDatosHistorial();

            do {
                Menu();

                opcion = pedirEntero("Elige una opción");
                switch (opcion) {

                    case 1:
                        registrarUsuario();
                        break;

                    case 2:
                        login();
                        break;

                    case 3:
                      
                        Menu();
                        break;

                    default:

                }

            } while (opcion != 4);

        } catch (Exception e) {
        }

    }
    
    //Este método nos va a cargar los datos que tengamos del Array de Sudokus
    
    public static void cargarDatos() throws IOException, JAXBException {
        BufferedReader br = new BufferedReader(new FileReader(txtsudokus));
        //Si el fichero xml no existe, utilizamos la tecnologia JAXB para marshallear los datos
        if (!fileSudokus.exists()) {
            JAXBContext contexto = JAXBContext.newInstance(Sudokus.class);
            Marshaller marshaller = contexto.createMarshaller();
            String line;
            String linea2;
            String linea3;

            try {
                //Se va leyendo línea por línea los datos
                //Y los asignamos a su variable correspondiente
                while ((line = br.readLine()) != null) {
                    linea2 = br.readLine();
                    linea3 = br.readLine();
                    Sudoku s = new Sudoku();
                    //Setteamos el objeto sudoku con los problema y la solución
                    s.setProblem(linea2);
                    s.setSolved(linea3);
                    String[] datos = line.split(" ");
                    int nivel = Integer.parseInt(datos[1]);
                    String description = datos[2];
                    s.setLevel(nivel);
                    s.setDescription(description);
                    //Añadimos el objeto al ArrayList de Sudokus
                    arraySudokus.getSudoku().add(s);

                }
                //Marshalleamos la estructura del array en el fichero xml,
                //Es decir, generamos información en el xml a base de la estructura de objetos
                marshaller.marshal(arraySudokus, fileSudokus);

            } catch (IOException ex) {
                Logger.getLogger(Practica4JAXB.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }
//Hace lo mismo que el método cargarDatos()
    public static void cargarDatosUsuario() {
        if (fileUsuarios.exists()) {
            try {
                JAXBContext contexto = JAXBContext.newInstance(Usuarios.class);
                Unmarshaller u = contexto.createUnmarshaller();
                arrayUsuarios = (Usuarios) u.unmarshal(fileUsuarios);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            arrayUsuarios = new Usuarios();
        }

    }
    //Hace lo mismo  que el método cargarDatos()
    public static void cargarDatosHistorial() throws FileNotFoundException, JAXBException {
    if (fileHistorial.exists()) {
        try {
            JAXBContext contexto = JAXBContext.newInstance(Historial.Historico.class);
            Unmarshaller u = contexto.createUnmarshaller();
            Historial arrayHistorial = (Historial) u.unmarshal(fileHistorial);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } } else {
            arrayUsuarios = new Usuarios();
        }

    }

 
    
    public static void registrarUsuario() {
        try {
            //Pedimos los datos para rellenar el formulario
            String nombreCompleto = pedirCadena("Tu nombre completo");
            String nombreUsuario = pedirCadena("Tu nombre de usuario");
            String password = pedirCadena("Password");

            //Creado constructor nuevo en Usuarios
            Usuario u = new Usuario();
            //Le seteamos los valores recogidos
            u.setNombreCompleto(nombreCompleto);
            u.setNombreUsuario(nombreUsuario);
            u.setPassword(password);
            //Lo añadimos al ArrayList de usarios
            arrayUsuarios.getUsuario().add(u);
            System.out.println("Usuario Registrado");
            //Marshalleamos la estructura de datos en el fichero XML de usuarios
            JAXBContext contexto = JAXBContext.newInstance(Usuarios.class);
            Marshaller m = contexto.createMarshaller();
            //Marshaleamos la información del Array de Usuarios al fichero XML de Usuarios
            m.marshal(arrayUsuarios, fileUsuarios);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
    //Menú una vez iniciada la sesión
    public static void MenuLogin() {
        System.out.println("[1] Modificar Contraseña");
        System.out.println("[2] Obtener Sudoku Random");
        System.out.println("[3] Sudoku Terminado");
        System.out.println("[4] Ver AVG Tiempo");
        System.out.println("[5] Cerrar Sesión");
    }

    public static void login() {
        int opcion2;
        //Pedimos datos para rellenar el formulario
        String nombreUsuario = pedirCadena("Nombre Usuario");
        String password = pedirCadena("Password");
        //Recorremos el ArrayList de usuarios
        for (Usuario u : arrayUsuarios.getUsuario()) {
            //Si el nombre de usuario y la contraseña son equivalentes al objeto del ArrayList
            if (u.getNombreUsuario().equalsIgnoreCase(nombreUsuario) && u.getPassword().equals(password)) {
                //En este String guardamos el nombre del usuario que está actualmente conectado
                //Para en un futuro poder gestionar la cuenta
                nombreUsuarioActual = nombreUsuario;
                System.out.println("Bienvenido " + u.getNombreCompleto());
                do {
                    
                    MenuLogin();
                    
                    opcion2 = pedirEntero("Elige una opción");

                    switch (opcion2) {

                        case 1:
                            modificarPassword();
                            break;

                        case 2:

                            break;

                        case 3:

                            break;

                        case 4:

                            break;

                    }
                } while (opcion2 != 5);

            } else {
                System.out.println("Usuario o contraseña incorrectos");
            }
        }
    }
    //Menú principal
    public static void Menu() {
        System.out.println("*******************************");
        System.out.println("");
        System.out.println("[1] Registrar Usuario");
        System.out.println("[2] Login");
        System.out.println("[3] Ranking");
        System.out.println("[4] Salir");
        System.out.println("");
        System.out.println("*******************************");
    }
    //Método para cambiar la contraseña del usuario logeado
    public static void modificarPassword() {
        //Pedimos los datos para rellenar el formulario
        String username = pedirCadena("Escribe el usuario");
        String newPassword = pedirCadena("Escribe la nueva contraseña");
       
        //Recorremos el ArrayList de usuarios
        for (Usuario u : arrayUsuarios.getUsuario()) {
            //Si el usuario corresponde con el que está logueado ahora mismo, nos entra en el if
            if (u.getNombreUsuario().equalsIgnoreCase(nombreUsuarioActual)) {
                //Seteamos la password nueva al objeto usuario
                u.setPassword(newPassword);
                System.out.println("Password Modificado");

                
             try {
                 
                 JAXBContext contexto = JAXBContext.newInstance(Usuarios.Usuario.class);   
                         Marshaller m = contexto.createMarshaller();
                         //Marshaleamos la información del Array de Usuarios al fichero XML de Usuarios
                         m.marshal(arrayUsuarios, fileUsuarios);
                         
                
            } catch (Exception e) {
            }
            } else {
                System.out.println("Usuario incorrecto");
            }
        }

    }

    public static String pedirCadena(String texto) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String cadena = "";
        do {
            try {
                System.out.println(texto);
                cadena = br.readLine();
                if (cadena.equals("")) {
                    System.out.println("No se puede dejar el campo en blanco.");
                }
            } catch (IOException ex) {
                System.out.println("Error de entrada / salida.");
            }
        } while (cadena.equals(""));
        return cadena;
    }

    public static int pedirEntero(String texto) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int num = 0;
        boolean error;
        do {
            try {
                System.out.println(texto);
                num = Integer.parseInt(br.readLine());
                error = false;
            } catch (IOException ex) {
                System.out.println("Error de entrada / salida.");
                error = true;
            } catch (NumberFormatException ex) {
                System.out.println("Debes introducir un n�mero entero.");
                error = true;
            }
        } while (error);
        return num;
    }

}
