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
import org.hibernate.Transaction;

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
                ArrayList<Producto> productos = new ArrayList<Producto>();
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
                        Set productoset = categorias.get(i).getProductos();
                        for (Iterator itt = productoset.iterator();itt.hasNext();){
                            productos.add((Producto) itt.next());
                        }
                        
			session.setAttribute("productos", productos);
			session.setAttribute("categoria", categorias.get(i).getNombre());
			dispatcher = request.getRequestDispatcher("platos.jsp");
			dispatcher.forward(request, response);		
		} else  if (op.equals("detail")) {
			int i = Integer.parseInt(request.getParameter("i"));
			productos = (ArrayList<Producto>) session.getAttribute("productos");
			Producto p = productos.get(i);
                        Set puntoset = p.getPuntos();
                        double cont=0, suma=0;
                        int oper;
                        for (Iterator itt = puntoset.iterator();itt.hasNext();){
                            Punto pun = (Punto) itt.next();
                            cont++;
                            suma += pun.getPuntos();
                        }
                        if(cont!=0){
                             oper = (int) Math.round(suma/cont);
                        }else{
                            oper =0;
                        }
                        
			session.setAttribute("producto", p);
			session.setAttribute("estrellas", oper);
			dispatcher = request.getRequestDispatcher("detail.jsp");
			dispatcher.forward(request, response);		
		} else  if (op.equals("rating")) {
                        Short rating = Short.parseShort(request.getParameter("rating"));
                        Producto producto = (Producto) session.getAttribute("producto");
                        Punto pun = new Punto(); //se puede hacer en una linea
                        pun.setId(1);
                        pun.setProducto(producto);
                        pun.setPuntos(rating);
                        Transaction t = singelton.beginTransaction();
                        singelton.persist(pun);
                        t.commit();
                        Query q = singelton.createQuery("select round(avg(puntos)) from Punto where producto.id = "+producto.getId());
                        List<Double> puntosList = (List<Double>) q.list();
                        Double puntod = puntosList.get(0);
                        int estrellas = puntod.intValue();
			session.setAttribute("estrellas", estrellas);
			dispatcher = request.getRequestDispatcher("detail.jsp");
			dispatcher.forward(request, response);		
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
