package net.sourceforge.plantuml.jsonexporter.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.Member;
import net.sourceforge.plantuml.cucadiagram.dot.DotData;


public class Entity extends Base {
	
	private String className;
	private String[] namespace;
	private String name; // avoiding name collision
	private String type;
	private EntityInfo inherits;
	private String stereotype;
	
	private ArrayList<Relation> requires = new ArrayList<Relation>();
	private ArrayList<Property> properties = new ArrayList<Property>();
	private ArrayList<Method> constructors = new ArrayList<Method>();
	private ArrayList<Method> methods = new ArrayList<Method>();
	
	public static Entity fromPlantUmlEntity(IEntity e, DotData data){
		
		Entity entity = new Entity();
		
		entity.className = findClassName(e.getCode());
		entity.namespace = findNamespace(e);
		entity.name = toFullName(entity.className, entity.namespace);
		entity.type = e.getType().name().toLowerCase();
		
		if(e.getStereotype() != null){
			entity.stereotype = e.getStereotype().getLabel().replaceAll("<<", "").replaceAll(">>", "");
		}
		
		List<Member> members = e.getFieldsToDisplay();
		if(members != null){
			for(Member member: members){
				entity.properties.add(Property.fromPlantUmlMember(member));
			}
		}
		
		members = e.getMethodsToDisplay();
		if(members != null){
			// search for constructors and other methods
			for(Member member: members){
				
				Method method = Method.fromPlantUmlMember(member);
				if(method.isConstructor(e.getCode())){
					entity.constructors.add(method);
				}else {
					entity.methods.add(method);
				}
			}
		}
		
		// check if entity relationship is inheritance
		IEntity iEntity = data.getInheritedEntity(e);
		if(iEntity != null){
			entity.inherits = EntityInfo.fromPlantUmlEntity(iEntity);
		}
		
		// export inheritance and requiring entities
		for (Link link: data.getLinks()) {

			Relation relation = new Relation();			
			if (entity.className.equals(findClassName(link.getEntity1().getCode()))) {
				entity.requires.add(relation.fromPlantUmlEntity(link, 2));
			} else if (entity.className.equals(findClassName(link.getEntity2().getCode()))) {
				entity.requires.add(relation.fromPlantUmlEntity(link, 1));				
			}
		}
		
		return entity;
	}

	public String getClassName() {
		return className;
	}

	public String getType() {
		return type;
	}

	public EntityInfo getSuperClass() {
		return inherits;
	}

	public String getStereotype() {
		return stereotype;
	}

	public ArrayList<Relation> getRequires() {
		return requires;
	}

	public ArrayList<Property> getProperties() {
		return properties;
	}

	public String[] getNamespace() {
		return namespace;
	}
	
	public String getName() {
		return name;
	}

	public ArrayList<Method> getConstructors() {
		return constructors;
	}

	public ArrayList<Method> getMethods() {
		return methods;
	}

}
