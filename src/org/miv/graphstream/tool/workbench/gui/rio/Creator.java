package org.miv.graphstream.tool.workbench.gui.rio;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;

import javax.swing.JPanel;

public class Creator
	extends JPanel
{
	private static final long serialVersionUID = 0x06008000L;

	protected static final BasicStroke 	selectionModeStroke = new BasicStroke( 2, BasicStroke.CAP_ROUND,  BasicStroke.JOIN_ROUND );
	protected static final BasicStroke 	STROKE  			= new BasicStroke( 10, BasicStroke.CAP_ROUND,  BasicStroke.JOIN_ROUND );
	protected static final BasicStroke 	BORDER  			= new BasicStroke( 3, BasicStroke.CAP_ROUND,  BasicStroke.JOIN_ROUND );
	protected static final BasicStroke 	OVER_STROKE  		= new BasicStroke( 20, BasicStroke.CAP_ROUND,  BasicStroke.JOIN_ROUND );
	
	protected static final Color		BASIC				= new Color( 1,1,1, 0.5f );
	protected static final Color		OVER				= new Color( 0, 0.5f, 0.95f, 0.5f );
	protected static final Color		SELECT				= new Color( 0.8f, 1, 0.2f, 0.5f );
	protected static final Color		overIOComponent		= new Color(1,0.6f,0,0.4f);
	
	protected static enum Mode
	{
		None,
		InputSelection,
		OutputSelection
	}
	
	GSLinker 			linker;
	
	int 				mousex = -1;
	int					mousey = -1;
	
	boolean 			input = false;
	boolean 			output = false;
	
	Line2D				left;
	Arc2D				center;
	Line2D				right;
	Path2D				shape;
	
	XShape				inputSelector;
	XShape				outputSelector;
	
	int					inputs = 3;
	int					outputs = 10;
	
	Mode				mode = Mode.None;
	
	AffineTransform		transform;
	
	static int			count = 1;
	
	IOComponent	over = null;
	
	public Creator( GSLinker l )
	{
		setSize( 120, 120 );
		setLocation(10,10);
		setOpaque(false);

		linker 		= l;
		
		transform	= new AffineTransform();
		
		left 		= new Line2D.Float( 10, 60, 45, 60 );
		right 		= new Line2D.Float( 75, 60, 110, 60 );
		center 		= new Arc2D.Float( 45, 45, 30, 30, 0, 360, Arc2D.OPEN );
			
		shape 		= new Path2D.Float();
			shape.append( left, false);
			shape.append( center, false );
			shape.append( right, false );
		
		inputSelector = new XShape();
		inputSelector.draw( "outter", new Arc2D.Float( 10, 10, 100, 100, 0, 360, Arc2D.OPEN ) );
		for( int i = 0; i < inputs; i++ )
		{
			inputSelector.fill( "selection#" + i, new Arc2D.Float( 
					getWidth() / 2 + 50 * (float) Math.cos( i * 2 * Math.PI / (float) inputs ) - 5, 
					getHeight() / 2 + 50 * (float) Math.sin( i * 2 * Math.PI / (float) inputs ) - 5, 
					10, 10, 0, 360, Arc2D.OPEN )
			);
		}
			
		outputSelector = new XShape();
		outputSelector.draw( "outter", new Arc2D.Float( 10, 10, 100, 100, 0, 360, Arc2D.OPEN ) );
		for( int i = 0; i < outputs; i++ )
		{
			outputSelector.fill( "selection#" + i, new Arc2D.Float( 
				getWidth() / 2 + 50 * (float) Math.cos( i * 2 * Math.PI / (float) outputs ) - 5, 
				getHeight() / 2 + 50 * (float) Math.sin( i * 2 * Math.PI / (float) outputs ) - 5, 
				10, 10, 0, 360, Arc2D.OPEN )
			);
		}
		
		addMouseMotionListener( new MouseMotionAdapter()
		{
			public void mouseMoved( MouseEvent e )
			{
				Creator.this.mousex = (int) ( ( e.getX() / (float) Creator.this.getWidth() ) * 3 );
				Creator.this.mousey = (int) ( ( e.getY() / (float) Creator.this.getHeight() ) * 3 );
				Creator.this.repaint();
			}
			
			public void mouseDragged( MouseEvent e )
			{
				Creator.this.over = null;
				
				if( mousex == 1 && mousey == 1 )
				{
					Creator.this.setLocation(
						Creator.this.getX() + e.getX() - Creator.this.getWidth() / 2,
						Creator.this.getY() + e.getY() - Creator.this.getHeight() / 2
					);
				}
				else if( mousex == 0 && mousey == 1 )
				{
					double theta = Math.atan2(
							Creator.this.getHeight() / 2 - e.getY(),
							Creator.this.getWidth() / 2  - e.getX()
					);
					
					Creator.this.transform.setToIdentity();
					Creator.this.transform.rotate(theta, Creator.this.getWidth() / 2, Creator.this.getHeight() / 2 );
					
					Creator.this.mode = Mode.InputSelection;
					
					repaint();
				}
				else if( mousex == 2 && mousey == 1 )
				{
					double theta = Math.atan2(
							e.getY() - Creator.this.getHeight() / 2,
							e.getX() - Creator.this.getWidth() / 2 
					);
					
					Creator.this.transform.setToIdentity();
					Creator.this.transform.rotate(theta, Creator.this.getWidth() / 2, Creator.this.getHeight() / 2 );
					
					Creator.this.mode = Mode.OutputSelection;
					
					repaint();
				}
			}
		});
		
		addMouseListener( new MouseAdapter()
		{
			public void mouseExited( MouseEvent e )
			{
				Creator.this.mousex = -1;
				Creator.this.mousey = -1;
				Creator.this.repaint();
			}
			
			public void mouseClicked( MouseEvent e )
			{
				switch(Creator.this.mousex)
				{
				case 0:
					Creator.this.input = ! Creator.this.input;
					if( Creator.this.over != null )
						Creator.this.over.setInput( Creator.this.input );
					
					break;
				case 1:
					if( Creator.this.mousey == 1 && Creator.this.over == null )
					{
						if( input && output )
							Creator.this.linker.addFilter( String.format( "creator:filter#%04d", count++ ) );
						else if( input )
							Creator.this.linker.addInput( String.format( "creator:input#%04d", count++ ) );
						else if( output )
							Creator.this.linker.addOutput( String.format( "creator:output#%04d", count++ ) );
					}
					
					break;
				case 2:
					Creator.this.output = ! Creator.this.output;
					if( Creator.this.over != null )
						Creator.this.over.setOutput( Creator.this.output );
					
					break;
				}
				
				Creator.this.repaint();
			}
			
			public void mouseReleased( MouseEvent e )
			{
				IOComponent ioc = Creator.this.linker.getNearestIOComponent(e.getX()+Creator.this.getX(),e.getY()+Creator.this.getY());
				
				if( ioc != null )
				{
					double d = Math.sqrt(
							Math.pow( Creator.this.getX() + Creator.this.getWidth() / 2 - ioc.getX() - ioc.getWidth() / 2, 2 ) +
							Math.pow( Creator.this.getY() + Creator.this.getHeight() / 2 - ioc.getY() - ioc.getHeight() / 2, 2 )
					);
					
					if( d < getWidth() / 3 )
					{
						Creator.this.over = ioc;
						Creator.this.input = ioc.input;
						Creator.this.output = ioc.output;
						
						Creator.this.setLocation(
								ioc.getX() + ioc.getWidth() / 2 - Creator.this.getWidth() / 2,
								ioc.getY() + ioc.getHeight() / 2 - Creator.this.getHeight() / 2
						);
					}
				}
				
				Creator.this.transform.setToIdentity();
				Creator.this.mode = Mode.None;
				Creator.this.repaint();
			}
		});
	}
	
	public void repaint()
	{
		if( getParent() != null )
			getParent().repaint( getLocation().x, getLocation().y, getWidth(), getHeight() );
		super.repaint();
	}
	
	public void paintComponent( Graphics g )
	{
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setRenderingHint( RenderingHints.KEY_INTERPOLATION,       RenderingHints.VALUE_INTERPOLATION_BICUBIC );
		g2d.setRenderingHint( RenderingHints.KEY_RENDERING,           RenderingHints.VALUE_RENDER_QUALITY );
		g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING,   RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
	    g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,        RenderingHints.VALUE_ANTIALIAS_ON );
	    g2d.setRenderingHint( RenderingHints.KEY_COLOR_RENDERING,     RenderingHints.VALUE_COLOR_RENDER_QUALITY );
	    g2d.setRenderingHint( RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY );
	    g2d.setRenderingHint( RenderingHints.KEY_STROKE_CONTROL,      RenderingHints.VALUE_STROKE_PURE );

	    if( mode == Mode.InputSelection )
	    {
	    	g2d.setStroke(selectionModeStroke);
	    	inputSelector.paint(g2d);
	    }
	    else if( mode == Mode.OutputSelection )
	    {
	    	g2d.setStroke(selectionModeStroke);
	    	outputSelector.paint(g2d);
	    }
	    
	    g2d.transform(transform);
	    g2d.setStroke(OVER_STROKE);
    	
	    if( ( ( mousex == 0 || input && mode == Mode.None ) ) || mode == Mode.InputSelection )
	    {
	    	g2d.setColor( input ? SELECT : OVER );
	    	g2d.draw(left);
	    }
	    
	    if( mousex == 1 )
	    {
	    	g2d.setColor( OVER );
	    	g2d.draw(center);
	    }
	    
	    if( ( ( mousex == 2 || output && mode == Mode.None ) ) || mode == Mode.OutputSelection )
	    {
	    	g2d.setColor( output ? SELECT : OVER );
	    	g2d.draw(right);
	    }
	    
		g2d.setColor( over != null ? overIOComponent : BASIC );
		g2d.setStroke(STROKE);
		g2d.draw(shape);
	}
}