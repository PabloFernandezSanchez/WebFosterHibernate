package controller;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.Query;
import org.hibernate.Session;

import pojos.Categoria;
import pojos.Producto;
import pojos.Punto;
import util.HibernateUtil;


/**
 * Servlet implementation class Controller
 */
@WebServlet("/Controller")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ArrayList<Categoria> categorias;
	private Categoria categoria;
	private List<Producto> productos;
	private Producto producto;
	private int i;
	private int estrellas;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Controller() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String op = request.getParameter("op");
		RequestDispatcher dispatcher;
                List<Categoria> categorias;
                ArrayList<Producto> productos2 = new ArrayList<Producto>();
                Session singelton = HibernateUtil.getSessionFactory().openSession();
		if (op.equals("inicio")) {
			Query q = singelton.createQuery("from Categoria"); 
                        categorias = q.list();
			session.setAttribute("categorias", categorias);
			dispatcher = request.getRequestDispatcher("home.jsp");
			dispatcher.forward(request, response);

		} else if (op.equals("dameproductos")) {
			categorias = (List<Categoria>) session.getAttribute("categorias");
                        int i = Integer.parseInt(request.getParameter("i"));
                        Set productos = categorias.get(i).getProductos();
                        for (Iterator itt = productos.iterator();itt.hasNext();){
                            productos2.add((Producto) itt.next());
                        }
                        
			session.setAttribute("productos", productos2);
			session.setAttribute("categoria", categorias.get(i).getNombre());
			dispatcher = request.getRequestDispatcher("platos.jsp");
			dispatcher.forward(request, response);		
		} else  if (op.equals("detail")) {
			//i = Integer.parseInt(request.getParameter("i"));
			//productos = (ArrayList<Producto>) session.getAttribute("productos");
			//producto = productos.get(i);
			//estrellas = new DAOPunto().getPuntos(producto.getId());
			//session.setAttribute("producto", producto);
			//session.setAttribute("estrellas", estrellas);
			//dispatcher = request.getRequestDispatcher("detail.jsp");
			//dispatcher.forward(request, response);		
		} else  if (op.equals("rating")) {
			//producto = (Producto)session.getAttribute("producto");
			//int puntos = Integer.parseInt(request.getParameter("rating"));
			//Punto punto = new Punto();
			//punto.setIdproducto(producto.getId());
			//punto.setPuntos(puntos);
			//new DAOPunto().addPunto(punto);
			//estrellas = new DAOPunto().getPuntos(producto.getId());
			//session.setAttribute("estrellas", estrellas);
			//dispatcher = request.getRequestDispatcher("detail.jsp");
			//dispatcher.forward(request, response);		
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
