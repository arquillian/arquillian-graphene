/**
 * JBoss, Home of Professional Open Source Copyright 2012, Red Hat, Inc. and individual contributors by the
 *
 * @authors tag. See the copyright.txt in the distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
var Graphene = Graphene || {};

Graphene.formSubmissionInterception = (function() {

    var lastSubmittedForm = null;
    var passThroughForm = null;
    var boundHandler;

    function submitHandler(e) {
        var form = e.target;

        /* when the submission should be passed through */
        if (passThroughForm === form) {
            passThroughForm = null;
            return true;
        } else {
            lastSubmittedForm = form;
            e.stopImmediatePropagation();
            return false;
        }
    }
    ;

    function reset() {
        lastSubmittedForm = null;
        boundHandler = null;
    }

    /* PUBLIC METHODS */
    return {
        bind : function() {
            if (!boundHandler) {
                boundHandler = $.proxy(submitHandler, this);
                $(document).bind('submit', boundHandler);
            }
        },

        unbind : function() {
            if (boundHandler) {
                $(document).unbind('submit', boundHandler);
                reset();
            }
        },

        isBound : function() {
            return !!boundHandler;
        },

        isSubmissionPaused : function() {
            return !!lastSubmittedForm;
        },

        getSubmittedForm : function() {
            return lastSubmittedForm;
        },

        continueSubmission : function() {
            passThroughForm = lastSubmittedForm;
            lastSubmittedForm.submit();
            lastSubmittedForm = null;
        }
    };
})();