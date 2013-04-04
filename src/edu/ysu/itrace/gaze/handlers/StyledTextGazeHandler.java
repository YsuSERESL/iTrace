package edu.ysu.itrace.gaze.handlers;

import java.util.Hashtable;
import java.util.Map;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchPartReference;

import edu.ysu.itrace.Gaze;
import edu.ysu.itrace.gaze.IGazeHandler;
import edu.ysu.itrace.gaze.IGazeResponse;


/**
 * Implements the gaze handler interface for a StyledText widget.
 */
public class StyledTextGazeHandler implements IGazeHandler {

	private IWorkbenchPartReference partRef;
	private StyledText targetStyledText;
	
	/**
	 * Constructs a new gaze handler for the target StyledText object within
	 * the workbench part specified by partRef.
	 */
	public StyledTextGazeHandler(Object target, IWorkbenchPartReference partRef){
		assert(target instanceof StyledText);
		this.targetStyledText = (StyledText)target;
		this.partRef = partRef;
	}
	
	
	@Override
	public IGazeResponse handleGaze(final int x, final int y, final Gaze gaze) {
		
		IGazeResponse response = new IGazeResponse(){
			
			private String name = partRef.getPartName();
			private String type = null;
			private Map<String,String> properties = new Hashtable<String,String>();
			
			// construct the type and properties for the response
			{
				try{
					int lineIndex = targetStyledText.getLineIndex(y);
					int lineOffset = targetStyledText.getOffsetAtLine(lineIndex);
					int offset = targetStyledText.getOffsetAtLocation(new Point(x, y));
					int col = offset - lineOffset;
					int lineHeight = targetStyledText.getLineHeight(offset);
					int fontHeight = targetStyledText.getFont().getFontData()[0].getHeight();
					
					this.properties.put("line-height", String.valueOf(lineHeight));
					this.properties.put("font-height", String.valueOf(fontHeight));
					this.properties.put("line", String.valueOf(lineIndex));
					this.properties.put("col", String.valueOf(col));
				}
				catch(Exception e){}
				
				this.type = "text";
			}
			
			@Override
			public String getName() {
				return this.name;
			}
			
			@Override
			public String getType() {
				return this.type;
			}
			
			@Override
			public Map<String, String> getProperties() {
				return this.properties;
			}
			
			@Override
			public Gaze getGaze(){
				return gaze;
			}
		};
		
		return (response.getType() != null ? response : null);
	}

}
