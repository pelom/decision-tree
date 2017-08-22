/*********************************
 * Author: Xue (Charlotte) Lin
 * Date: 2015/04/01
 *********************************/
package br.pjsign.dt.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.pjsign.dt.Attribute;
import br.pjsign.dt.Instance;
import br.pjsign.dt.impl.AttributeDefault;
import br.pjsign.dt.impl.InstanceDefault;

public class InputData {

	private Attribute targetAttribute;
	private List<Attribute> attributeSet;
	private List<Instance> instanceSet;
	private final String fileName;

	private boolean read = false;

	/**
	 * Constructor to initialize fields.
	 * @param fileName: file name of input data file
	 * @throws IOException 
	 */
	public InputData(String fileName) throws IOException {
		this.fileName = fileName;
		this.attributeSet = new ArrayList<Attribute>();
		this.instanceSet = new ArrayList<Instance>();
	}

	public void load() throws IOException {
		final Scanner in = new Scanner(new File(fileName));

		// Pass the first two line of input data.
		if (!in.hasNextLine()) {
			throw new IOException("Invalid input format");
		}
		in.nextLine();
		if (!in.hasNextLine()) {
			throw new IOException("Invalid input format");
		}
		in.nextLine();

		String line = in.nextLine();
		// Put all attributes into attributeSet
		while (!line.equals("")) {
			final Attribute attr = createAttribute(line);
			attributeSet.add(attr);
			line = in.nextLine();
		}

		this.targetAttribute = attributeSet.get(attributeSet.size() - 1);

		// Pass the next two line
		if (!in.hasNextLine()) throw new IOException("Invalid input format");
		line = in.nextLine();

		// Put all instances into instanceSet
		while (in.hasNextLine()) {
			line = in.nextLine();
			final String[] lineArr = line.split(",");
			final Instance item = createInstance(lineArr);
			instanceSet.add(item);
		}
		this.read = true;
	}

	public Instance createInstance(final String[] line) {
		final Instance instance = new InstanceDefault();
		for (int i = 0; i < line.length; i++) {
			final Attribute at = this.attributeSet.get(i);
			instance.setAttribute(at.getName(), line[i]);
		}

		return instance;
	}

	public Attribute createAttribute(final String line) throws IOException {

		// lineArr should have three elements.
		// lineArr[1] is attribute name; lineArr[2] is attribute value
		final String[] lineArr = line.split("\\s+");

		if (lineArr.length != 3) {
			throw new IOException("Invalid input format");
		}

		final Attribute attr = new AttributeDefault(lineArr[1], lineArr[2]);
		return attr;
	}

	public List<Attribute> getAttributeSet() {
		attributeSet.remove(attributeSet.size() - 1);
		return attributeSet;
	}
	
	public List<Instance> getInstanceSet(){
		return instanceSet;
	}
	
	public Attribute getTargetAttribute() {
		return targetAttribute;
	}

	public boolean isRead() {
		return read;
	}
}