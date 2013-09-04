/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
module("Form Submission Interception");

(function() {

    var fsi = Graphene.formSubmissionInterception;
    var form;
    var submitButton;

    QUnit.testStart = function() {
        form = document.getElementById("form1");
        submitButton = $(document.getElementById("form1:submit"));
        window.form1Submitted = false;
        if (fsi) {
            try {
                fsi.unbind();
            } catch (e) {
            }
        }
    };

    // TESTS

    test("accessor object not null", function() {
        ok(fsi);
    });

    test("form can be submitted", function() {
        // when
        submitButton.click();
        // then
        ok(window.form1Submitted, "form should be submitted");
    });
    
    test("FSI provides information whenever it is bound", function() {
        ok(!fsi.isBound());
        fsi.bind();
        ok(fsi.isBound());
        fsi.unbind();
        ok(!fsi.isBound());
    });

    test("form submission can be intercepted", function() {
        // when
        fsi.bind()
        submitButton.click();

        // then
        ok(!window.form1Submitted);
    });
    
    test("FSI provides information whenever the form submission is paused", function() {
        // having
        ok(!fsi.isSubmissionPaused());
        
        // when
        fsi.bind();
        ok(!fsi.isSubmissionPaused());
        
        submitButton.click();
        ok(fsi.isSubmissionPaused());

        // then
        ok(!window.form1Submitted);
    });

    test("when form submission is intercepted then it can be continued", function() {
        fsi.bind()
        submitButton.click();
        ok(!window.form1Submitted);

        // when
        fsi.continueSubmission();
        ok(window.form1Submitted);
        ok(!fsi.getSubmittedForm());
        ok(!fsi.getSubmittedForm());
    });

})();