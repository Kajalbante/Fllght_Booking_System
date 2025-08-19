import React, { useState } from 'react';
import { deleteFlight } from '../Service/flightService';
import '../Style/deleteflight.css';

const DeleteFlight = ({ id, onFlightDeleted }) => {
    const [showConfirmation, setShowConfirmation] = useState(false);
    const [isDeleting, setIsDeleting] = useState(false);
    const [error, setError] = useState(null);

    const handleDeleteClick = () => {
        setShowConfirmation(true);
    };

    const handleConfirmDelete = async () => {
        setIsDeleting(true);
        setError(null);
        
        try {
            await deleteFlight(id);
            if (onFlightDeleted) onFlightDeleted();
        } catch (err) {
            console.error('Error deleting flight:', err);
            setError('Failed to delete flight. Please try again.');
        } finally {
            setIsDeleting(false);
            setShowConfirmation(false);
        }
    };

    const handleCancelDelete = () => {
        setShowConfirmation(false);
    };

    return (
        <>
            <button 
                className="delete-btn"
                onClick={handleDeleteClick}
                disabled={isDeleting}
            >
                {isDeleting ? (
                    <>
                        <span className="loading-spinner"></span>
                        Deleting...
                    </>
                ) : (
                    'Delete'
                )}
            </button>

            {showConfirmation && (
                <div className="confirmation-dialog">
                    <div className="confirmation-content">
                        <h3 className="confirmation-title">Confirm Deletion</h3>
                        <p className="confirmation-message">
                            Are you sure you want to delete this flight? This action cannot be undone.
                        </p>
                        <div className="confirmation-buttons">
                            <button 
                                className="cancel-btn"
                                onClick={handleCancelDelete}
                                disabled={isDeleting}
                            >
                                Cancel
                            </button>
                            <button 
                                className="confirm-btn"
                                onClick={handleConfirmDelete}
                                disabled={isDeleting}
                            >
                                {isDeleting ? (
                                    <>
                                        <span className="loading-spinner"></span>
                                        Deleting...
                                    </>
                                ) : (
                                    'Delete'
                                )}
                            </button>
                        </div>
                        {error && <p className="error-message" style={{ marginTop: '1rem' }}>{error}</p>}
                    </div>
                </div>
            )}
        </>
    );
};

export default DeleteFlight;