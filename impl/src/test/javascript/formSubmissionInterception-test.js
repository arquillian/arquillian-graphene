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