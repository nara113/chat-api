import React, {useEffect, useState} from "react";
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import axios from "axios";

const CreateChatRoomModal = ({show, onHide}) => {
    const search = () => {
        axios.get(`/api/v1/friends`)
            .then(res => {
                console.log(res.data.data)

            })
            .catch(error => {
                console.log(error.response.data);
            })

    }

    useEffect(() => {
        search();
    }, [])

    return (
        <Modal
            show={show}
            onHide={onHide}
            size="lg"
            aria-labelledby="contained-modal-title-vcenter"
            backdrop="static"
            centered
        >
            <Modal.Header closeButton>
                <Modal.Title id="contained-modal-title-vcenter">
                </Modal.Title>
            </Modal.Header>
            <Modal.Body>

            </Modal.Body>
            <Modal.Footer>
                <Button variant="primary">채팅방 생성</Button>
            </Modal.Footer>
        </Modal>
    );
}

export default CreateChatRoomModal;