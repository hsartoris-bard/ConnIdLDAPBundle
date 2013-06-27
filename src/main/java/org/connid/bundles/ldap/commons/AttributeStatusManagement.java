/**
 * ====================
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2008-2009 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2011-2013 Tirasa. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License("CDDL") (the "License"). You may not use this file
 * except in compliance with the License.
 *
 * You can obtain a copy of the License at https://oss.oracle.com/licenses/CDDL
 * See the License for the specific language governing permissions and limitations
 * under the License.
 *
 * When distributing the Covered Code, include this CDDL Header Notice in each file
 * and include the License file at https://oss.oracle.com/licenses/CDDL.
 * If applicable, add the following below this CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * ====================
 */
package org.connid.bundles.ldap.commons;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import org.identityconnectors.common.logging.Log;

/**
 * Simple attribute-based status management implementation, meant for easy override.
 */
public class AttributeStatusManagement extends StatusManagement {

    private static final Log LOG = Log.getLog(AttributeStatusManagement.class);

    protected String getStatusAttrName() {
        return "description";
    }

    protected String getStatusAttrActiveValue() {
        return "Active";
    }

    protected String getStatusAttrInactiveValue() {
        return "Inactive";
    }

    @Override
    public void setStatus(final boolean status, final Attributes attributes,
            final List<String> posixGroups, final List<String> ldapGroups) {

        LOG.ok("Calling setStatus {0}", status);

        Attribute statusAttrr = attributes.get(getStatusAttrName());
        if (statusAttrr == null) {
            statusAttrr = new BasicAttribute(getStatusAttrName());
            attributes.put(statusAttrr);
        }

        statusAttrr.add(status ? getStatusAttrActiveValue() : getStatusAttrInactiveValue());
    }

    @Override
    public Boolean getStatus(final Attributes attributes,
            final List<String> posixGroups, final List<String> ldapGroups) {

        Boolean status = null;

        final Attribute description = attributes.get(getStatusAttrName());
        if (description != null) {
            try {
                final Object value = description.get();
                if (value != null) {
                    status = getStatusAttrActiveValue().equals(value.toString());
                }
            } catch (NamingException ignore) {
                status = null;
            }
        }

        return status;
    }

    @Override
    public Set<String> getOperationalAttributes() {
        return Collections.singleton(getStatusAttrName());

    }
}
