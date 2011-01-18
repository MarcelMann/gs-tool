/*
 * Copyright 2006 - 2011 
 *     Julien Baudry	<julien.baudry@graphstream-project.org>
 *     Antoine Dutot	<antoine.dutot@graphstream-project.org>
 *     Yoann Pigné		<yoann.pigne@graphstream-project.org>
 *     Guilhelm Savin	<guilhelm.savin@graphstream-project.org>
 * 
 * This file is part of GraphStream <http://graphstream-project.org>.
 * 
 * GraphStream is a library whose purpose is to handle static or dynamic
 * graph, create them from scratch, file or any source and display them.
 * 
 * This program is free software distributed under the terms of two licenses, the
 * CeCILL-C license that fits European law, and the GNU Lesser General Public
 * License. You can  use, modify and/ or redistribute the software under the terms
 * of the CeCILL-C license as circulated by CEA, CNRS and INRIA at the following
 * URL <http://www.cecill.info> or under the terms of the GNU LGPL as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-C and LGPL licenses and that you accept their terms.
 */
package org.graphstream.tool.workbench.gui;

import org.graphstream.graph.Graph;
import org.graphstream.stream.ElementSink;
import org.graphstream.tool.workbench.WCore;
import org.graphstream.tool.workbench.cli.CLI;
import org.graphstream.tool.workbench.event.*;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.DefaultComboBoxModel;

/**
 * Panel which will be used to display graph informations.
 * 
 * @author Guilhelm Savin
 *
 */
public class WGraphInfo extends JPanel 
	implements ContextChangeListener, ElementSink, ItemListener
{
	public static final long serialVersionUID = 0x040100000001L;
	
	protected WCore core;
	protected CLI			cli;
	protected JLabel		graphId;
	protected JLabel		nodesCount;
	protected JLabel		edgesCount;
	protected Graph			listeningGraph;
	protected GraphsModel	graphsModel;
	
	@SuppressWarnings("unused")
	private WGraphInfo()
	{
		throw new Error( "bad way" );
	}
	
	public WGraphInfo( CLI cli )
	{
		this.cli = cli;
		this.core = cli.getCore();
		this.listeningGraph = null;
		
		JPanel tmp = new JPanel();
		tmp.setLayout( new GridLayout( 4, 1 ) );
		
		this.graphId = new JLabel();
		this.nodesCount = new JLabel();
		this.edgesCount = new JLabel();
		this.graphsModel = new GraphsModel();
		
		JComboBox comboBox = new JComboBox( graphsModel );
		comboBox.addItemListener( this );
		
		tmp.add( comboBox );
		tmp.add( graphId );
		tmp.add( nodesCount );
		tmp.add( edgesCount );
		
		setLayout( new BorderLayout() );
		
		add( tmp, BorderLayout.NORTH );
		
		tmp = new JPanel();
		tmp.setLayout( new BorderLayout() );
		WElementList nodeList = WElementList.createNodeList(core); 
		JScrollPane nodeListSP = new JScrollPane(nodeList);
		tmp.setPreferredSize(new java.awt.Dimension(150,300));
		tmp.add( nodeListSP, BorderLayout.CENTER );
		
		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab( "Nodes", tmp );
		
		tmp = new JPanel();
		tmp.setLayout( new BorderLayout() );
		WElementList edgeList = WElementList.createEdgeList(core); 
		JScrollPane edgeListSP = new JScrollPane(edgeList);
		tmp.setPreferredSize(new java.awt.Dimension(150,300));
		tmp.add( edgeListSP, BorderLayout.CENTER );
		
		tabs.addTab( "Edges", tmp );
		
		add( tabs, BorderLayout.CENTER );
		
		updateGraphInformation();
		
		core.addContextChangeListener( this );
		core.addWorkbenchListener( graphsModel );
		
		setPreferredSize( new java.awt.Dimension( 150, 300 ) );
	}
	
	protected void updateGraphInformation()
	{
		if( core.getActiveContext() != null && core.getActiveContext().getGraph() != null )
		{
			Graph g = core.getActiveContext().getGraph();
			
			setGraphId( g.getId() );
			setNodesCount( g.getNodeCount() );
			setEdgesCount( g.getEdgeCount() );
		}
		else
		{
			resetInformations();
		}
	}
	
	protected void resetInformations()
	{
		graphId.setText( "" );
		nodesCount.setText( "" );
		edgesCount.setText( "" );
	}
	
	protected void setGraphId( String id )
	{
		graphId.setText( "id: " + id );
	}
	
	protected void setNodesCount( int n )
	{
		nodesCount.setText( String.format( "nodes: %d", n ) );
	}
	
	protected void setEdgesCount( int n )
	{
		edgesCount.setText( String.format( "edges: %d", n ) );
	}
	
// ContextChangeListener implementation
	
	public void contextChanged( ContextEvent e )
	{
		Graph g = e.getContext() == null ? null : e.getContext().getGraph();
		
		if( g != listeningGraph )
		{
			if( listeningGraph != null ) 
				listeningGraph.removeElementSink( this );
			
			if( g != null )
				g.addElementSink( this );
			
			listeningGraph = g;
		}
		
		updateGraphInformation();
	}
	
// GraphListener implementation
	
	public void nodeAdded( String graphId, long timeId, String id )
	{
		updateGraphInformation();
	}

	public void edgeAdded( String graphId, long timeId, String id, String src, String trg, boolean directed )
	{
		updateGraphInformation();
	}

	public void nodeRemoved( String graphId, long timeId, String id )
	{
		Graph g = core.getActiveContext().getGraph();
		setNodesCount( g.getNodeCount() - 1 );
	}
	
	public void edgeRemoved( String graphId, long timeId, String id )
	{
		Graph g = core.getActiveContext().getGraph();
		setEdgesCount( g.getEdgeCount() - 1 );
	}
	
	public void graphCleared( String graph, long timeId ) {}
	
	public void stepBegins( String graphId, long timeId, double time )
	{
		
	}
	/*
	public void attributeChanged( Element element, String attribute,
			Object oldValue, Object newValue ) {}
	*/
// ItemListener implementation
	
	public void itemStateChanged( ItemEvent e )
	{
		if( graphsModel.getSelectedItem() == null ) return;
		cli.execute( String.format( "select graph \"%s\"", graphsModel.getSelectedItem() ) );
	}
	
// Model used in ComboBox
	
	/**
	 * Model used to managed graph.
	 * 
	 * @author Guilhelm Savin
	 * 
	 */
	class GraphsModel extends DefaultComboBoxModel
		implements WorkbenchListener
	{
		public static final long serialVersionUID = 0x040100010001L;
		
		public void contextAdded( ContextEvent ce )
		{
			if( ce.getContext() != null )
				addElement( ce.getContext().getGraph().getId() );
		}
		
		public void contextRemoved( ContextEvent ce )
		{
			if( ce.getContext() != null )
				removeElement( ce.getContext().getGraph().getId() );
		}
		
		public void contextChanged( ContextEvent ce )
		{
			if( ce.getContext() != null )
				setSelectedItem( ce.getContext().getGraph().getId() );
			else
				setSelectedItem( null );
		}
		
		public void contextShow( ContextEvent ce )
		{
			
		}
	}

	public void stepBegins(Graph graph, double time)
	{
	}
}
