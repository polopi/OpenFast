package org.openfast.template.loader;

import org.openfast.Global;
import org.openfast.QName;
import org.openfast.template.Field;
import org.openfast.template.Scalar;
import org.openfast.template.Sequence;
import org.openfast.template.type.Type;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class SequenceParser extends AbstractFieldParser {

	private FieldParser sequenceLengthParser = new ScalarParser("length") {
		protected Type getType(Element fieldNode, ParsingContext context) {
			return Type.U32;
		}

		protected QName getName(Element fieldNode, ParsingContext context) {
			if (context.getName() == null)
				return Global.createImplicitName(context.getParent().getName());
			return context.getName();
		}
	};
	
	public SequenceParser() {
		super("sequence");
	}
	
	protected Field parse(Element sequenceElement, boolean optional, ParsingContext context) {
		Sequence sequence = new Sequence(context.getName(),
		            parseSequenceLengthField(sequenceElement, optional, context),
		            GroupParser.parseFields(sequenceElement, context), optional);
		GroupParser.parseMore(sequenceElement, sequence.getGroup(), context);
		return sequence;
	}

    /**
     * 
     * @param sequence The dom element object
     * @param sequenceName Name of the sequence to which this lenght field belongs
     * @param optional Determines if the Scalar is required or not for the data
     * @return Returns null if there are no elements by the tag length, otherwise 
     */
    private Scalar parseSequenceLengthField(Element sequence, boolean optional, ParsingContext parent) {
        NodeList lengthElements = sequence.getElementsByTagName("length");

        if (lengthElements.getLength() == 0) {
            return null;
        }

        Element length = (Element) lengthElements.item(0);
        return (Scalar) sequenceLengthParser.parse(length, parent);
    }

}
