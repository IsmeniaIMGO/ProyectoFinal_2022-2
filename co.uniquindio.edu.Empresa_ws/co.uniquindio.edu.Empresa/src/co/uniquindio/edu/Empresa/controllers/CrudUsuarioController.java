package co.uniquindio.edu.Empresa.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;
import co.uniquindio.edu.Empresa.application.Aplicacion;
import co.uniquindio.edu.Empresa.exceptions.UsuarioException;
import co.uniquindio.edu.Empresa.model.*;
import co.uniquindio.edu.Empresa.persistence.Persistencia;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class CrudUsuarioController {
	
	/**
	 * atributos
	 */
	Singleton singleton = Singleton.getInstance();
	Aplicacion aplicacion;
	Empresa empresa = singleton.empresa;
	
	//Metodo set de aplicacion
	public void setAplicacion(Aplicacion aplicacion){
		this.aplicacion = aplicacion;
		singleton.setAplicacion(aplicacion);
	}
	
	//instancia de una lista para la tableview
	public ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnEliminarUsuario"
    private Button btnEliminarUsuario; // Value injected by FXMLLoader

    @FXML // fx:id="btnIrSubastas"
    private Button btnIrLogin; // Value injected by FXMLLoader

    @FXML // fx:id="col_IdUsuario"
    private TableColumn<Usuario, String> col_IdUsuario; // Value injected by FXMLLoader

    @FXML // fx:id="txtIdUsuario"
    private TextField txtIdUsuario; // Value injected by FXMLLoader

    @FXML // fx:id="tblUsuarios"
    private TableView<Usuario> tblUsuarios; // Value injected by FXMLLoader

    @FXML // fx:id="btnSalirUsuario"
    private Button btnSalirUsuario; // Value injected by FXMLLoader

    @FXML // fx:id="txtNombreUsuario"
    private TextField txtNombreUsuario; // Value injected by FXMLLoader

    @FXML // fx:id="cbkComprador"
    private CheckBox cbkComprador; // Value injected by FXMLLoader

    @FXML // fx:id="col_EdadUsuario"
    private TableColumn<Usuario, String> col_EdadUsuario; // Value injected by FXMLLoader

    @FXML // fx:id="txtEdadUsuario"
    private TextField txtEdadUsuario; // Value injected by FXMLLoader

    @FXML // fx:id="btnActualizarUsuario"
    private Button btnActualizarUsuario; // Value injected by FXMLLoader

    @FXML // fx:id="btnCrearUsuario"
    private Button btnCrearUsuario; // Value injected by FXMLLoader

    @FXML // fx:id="col_TipoUsuario"
    private TableColumn<Usuario, TipoUsuario> col_TipoUsuario; // Value injected by FXMLLoader

    @FXML // fx:id="col_NombreUsuario"
    private TableColumn<Usuario, String> col_NombreUsuario; // Value injected by FXMLLoader

    @FXML // fx:id="cbkAnunciante"
    private CheckBox cbkAnunciante; // Value injected by FXMLLoader
    
    @FXML
    private Button limpiarCampos;
    
    @FXML
    void CrearUsuario(ActionEvent event) throws Exception{
    	 crearUsuario();
    	 observarDatos();
    	 limpiarCampos();
    	 
    }

	@FXML
    void ActualizarUsuario(ActionEvent event) throws Exception {
		 actualizarUsuario();
		 observarDatos();
		 limpiarCampos();
    }
	
	@FXML
    void EliminarUsuario(ActionEvent event) throws UsuarioException, FileNotFoundException, IOException {
		eliminarUsuario();
		observarDatos();
		limpiarCampos();
    }

	@FXML
    void AbrirLogin(ActionEvent event) {    	
    	singleton.mostrarLogin("/co/uniquindio/edu/Empresa/views/Login.fxml");

    }

    @FXML
    void cerrarVentanaUsuario(ActionEvent event) {
    	Stage thisStage = (Stage)((Node) event.getSource()).getScene().getWindow();
    	thisStage.close();
    }
    
    @FXML
    void limpiarCampos(ActionEvent event) {
    	limpiarCampos();
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() throws FileNotFoundException, IOException {
		limpiarCampos();
		seleccionarElemento();
		observarDatos();
    }

    //----------------------------METODOS---------------------------------------
    //--------------------------------------------------------------------------
    /**
     * Metodo que me permite crear un usuario obteniendo la informacion
     * de los campos de texto y con la logica creada en la clase empresa
     * @throws Exception
     */
	private void crearUsuario() throws Exception {
		String id = txtIdUsuario.getText();
		String nombre = txtNombreUsuario.getText();
		int edad = Integer.parseInt(txtEdadUsuario.getText());
		TipoUsuario tipoUsuario = null;
		
		if (cbkAnunciante.isSelected()) {
			tipoUsuario = tipoUsuario.ANUNCIANTE;
		}else {
			if (cbkComprador.isSelected()) {
				tipoUsuario = tipoUsuario.COMPRADOR;
			}
		}			
	singleton.crearUsuario(id, nombre, edad, tipoUsuario);
	singleton.mostrarMensaje("Operacion exitosa", "Usuario creado", "usuario: "+ id +"\n" + "contrase�a: "+ nombre, AlertType.INFORMATION);
	
	singleton.guardarUsuarios(singleton.listaUsuarios());
	singleton.guardarRegistroLog("usuario creado", 1, "crearUsuario");

	}
	
	/**
	 * metodo que me permite actualizar un usuario ya creado, obteniendo
	 * la informacion de los campos de texto y con la logica de la clase Empresa
	 * @throws Exception
	 */
	private void actualizarUsuario() throws Exception {
    	String id = txtIdUsuario.getText();
		String nuevoNombre = txtNombreUsuario.getText();
		int nuevaEdad = Integer.parseInt(txtEdadUsuario.getText());
		TipoUsuario nuevoTipo = null;
		
		if (cbkAnunciante.isSelected()) {
			nuevoTipo = nuevoTipo.ANUNCIANTE;
		}else {
			if (cbkComprador.isSelected()) {
				nuevoTipo = nuevoTipo.COMPRADOR;
			}
		}	
		
		singleton.actualizarUsuario(id, nuevoNombre, nuevaEdad, nuevoTipo);
		singleton.guardarUsuarios(singleton.listaUsuarios());
    	singleton.mostrarMensaje("Operacion exitosa", "Usuario actualizado", "operacion exitosa", AlertType.INFORMATION);
    	singleton.guardarRegistroLog("usuario actualizado", 1, "actualizarUsuario");
    	
	}
	
	/**
	 * metodo que me permite eliminar un usuario ya creado,
	 * obteniendo su id y con la logica de la clase Empresa
	 * @throws UsuarioException
	 * @throws IOException 
	 */
    private void eliminarUsuario() throws UsuarioException, IOException {
	    String id = txtIdUsuario.getText();
	    singleton.eliminarUsuario(id);
	    singleton.guardarRegistroLog("usuario eliminado", 1, "eliminarUsuario");
	    singleton.guardarUsuarios(singleton.listaUsuarios());
	}
	
    /**
     * Metodo que me permite colocar los valores 
     * de una lista en la tabla que el usuario va a observar
     * @throws IOException 
     * @throws FileNotFoundException 
     */
	private void observarDatos(){
    	col_IdUsuario.setCellValueFactory(new PropertyValueFactory<>("id"));
    	col_NombreUsuario.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    	col_EdadUsuario.setCellValueFactory(new PropertyValueFactory<>("edad"));
    	col_TipoUsuario.setCellValueFactory(new PropertyValueFactory<>("tipoUsuario"));
    	//hay que hacer validacion de que no se creen con el mismo id para arraylist de persistencia
    	//listaUsuarios.setAll(empresa.getListaUsuarios());
    	
    	listaUsuarios.setAll(singleton.listaUsuarios());
    	tblUsuarios.setItems(listaUsuarios);
    	  	
	}
	
	/**
	 * metodo que me permite seleccionar un elemnto de la tabla
	 * y devolver la informacion a los campos de texto
	 */
	private void seleccionarElemento(){
		tblUsuarios.getSelectionModel().selectedItemProperty().addListener(
    			new ChangeListener<Usuario>() {
    				@Override
    				public void changed(ObservableValue<? extends Usuario> arg0, Usuario oldValue, Usuario usuarioSeleccionado ){
    					if (usuarioSeleccionado != null) {
    						txtIdUsuario.setText(usuarioSeleccionado.getId());
    						txtNombreUsuario.setText(usuarioSeleccionado.getNombre());
    						txtEdadUsuario.setText(String.valueOf(usuarioSeleccionado.getEdad()));
    						if (usuarioSeleccionado.getTipoUsuario() == TipoUsuario.ANUNCIANTE) {
    							cbkAnunciante.setSelected(true);
    							cbkComprador.setSelected(false);
    						}else {
    							if (usuarioSeleccionado.getTipoUsuario() == TipoUsuario.COMPRADOR) {
    								cbkComprador.setSelected(true);
    								cbkAnunciante.setSelected(false);
    							}
    						}
    					}
    				}
    			}		
    	);	
	}
	
	/**
	 * metodod que me permite limpiar los campos de texto
	 * y asignar informacion para el usuario
	 */
	private void limpiarCampos() {
    	txtIdUsuario.setText("");
    	txtIdUsuario.setPromptText("Ingrese un nuevo Id");
    	txtNombreUsuario.setText("");
    	txtNombreUsuario.setPromptText("Ingrese un nombre");
    	txtEdadUsuario.setText("");
    	txtEdadUsuario.setPromptText("Ingrese una edad mayor a 18 a�os");
    	cbkAnunciante.setSelected(false);;
    	cbkComprador.setSelected(false);	
    	
    	tblUsuarios.getSelectionModel().clearSelection();
		}
}




