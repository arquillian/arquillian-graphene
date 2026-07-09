package org.arquillian.graphene.rusheye.fragments;

import org.arquillian.graphene.rusheye.annotation.RushEye;
import org.arquillian.graphene.rusheye.annotation.Snap;
import org.arquillian.graphene.rusheye.comparator.Ocular;
import org.arquillian.graphene.rusheye.comparator.OcularResult;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.fragment.Root;

@Snap("RichFaceLeftMenu.png")
public class RichFaceLeftMenu {
    
    @Root
    private GrapheneElement root;
    
    @RushEye
    private Ocular ocular;
    
    public OcularResult compare(){
        return this.ocular.element(root)
                   .compare();
    }
}
